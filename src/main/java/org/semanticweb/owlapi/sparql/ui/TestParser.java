/*
 * This file is part of the OWL API.
 *  
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *  
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, The University of Manchester
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.semanticweb.owlapi.sparql.ui;

//import org.semanticweb.HermiT.Reasoner;

import java.util.Optional;
import com.google.common.base.Stopwatch;
import de.derivo.sparqldlapi.QueryEngine;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.io.WriterDocumentTarget;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.sparql.algebra.AlgebraPrettyPrinter;
import org.semanticweb.owlapi.sparql.api.Axiom;
import org.semanticweb.owlapi.sparql.api.SPARQLQueryResult;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.parser.SPARQLParserImpl;
import org.semanticweb.owlapi.sparql.parser.tokenizer.SPARQLTokenizer;
import org.semanticweb.owlapi.sparql.parser.tokenizer.impl.SPARQLTokenizerJavaCCImpl;
import org.semanticweb.owlapi.sparql.reasoner.SPARQLAssertedReasoner;
import org.semanticweb.owlapi.sparql.sparqldl.SPARQLDLQueryEngine;
import org.semanticweb.owlapi.sparql.syntax.ConstructQuery;
import org.semanticweb.owlapi.sparql.syntax.Query;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 02/03/2012
 */
public class TestParser {


    public static final String SPARQLEXAMPLE = "sparqlexample";

    private static OWLReasoner reasoner;

    private static SPARQLDLQueryEngine queryEngine;


    public static void main(String[] args) throws Exception {
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

            IRI iri;

            if(args.length == 0) {
                FileDialog chooser = new FileDialog(new Frame());
                chooser.setTitle("Select ontology");
                chooser.setVisible(true);
                if(chooser.getFile() == null) {
                    System.exit(0);
                }
                File theFile = new File(new File(chooser.getDirectory()), chooser.getFile());

                iri = IRI.create(theFile);
            }
            else {
                String location = args[0];
                if(location.startsWith("http://")) {
                    iri = IRI.create(args[0]);
                }
                else {
                    File file = new File(location);
                    manager.addIRIMapper(new AutoIRIMapper(file.getParentFile(), false));
                    iri = IRI.create(file);
                }

            }

            final OWLOntology rootOntology = manager.loadOntologyFromOntologyDocument(iri);
            System.out.println("Loaded ontology: " + rootOntology);
            System.out.println("Creating reasoner...");
            Configuration configuration = new Configuration();
//            reasoner = new Reasoner(configuration, rootOntology);
            reasoner = new SPARQLAssertedReasoner(rootOntology);
            System.out.println("    .... done");
            System.out.println("Precomputing class hierarchy...");
            reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
            System.out.println("    .... done");
            QueryEngine qe = QueryEngine.create(manager, reasoner);
            queryEngine = new SPARQLDLQueryEngine(manager.getOWLDataFactory(), qe);

            JFrame f = new JFrame();
            final JTextComponent textArea = new SPARQLEditor(new OWLOntologyProvider() {
                public OWLOntology getOntology() {
                    return rootOntology;
                }
            });
            Preferences p = Preferences.userNodeForPackage(TestParser.class);
            textArea.setText(p.get(SPARQLEXAMPLE, ""));
            JPanel panel = new JPanel(new BorderLayout());
            f.getContentPane().setLayout(new BorderLayout());
            f.getContentPane().add(panel);
            panel.add(new JScrollPane(textArea));
            panel.add(new JButton(new AbstractAction("Parse") {
                public void actionPerformed(ActionEvent e) {
                    parse(rootOntology, textArea.getText());
                }
            }), BorderLayout.SOUTH);
            f.pack();
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    String text = textArea.getText();
                    Preferences p = Preferences.userNodeForPackage(TestParser.class);
                    p.put(SPARQLEXAMPLE, text);
                }
            });
            f.setVisible(true);
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }
    }

    private static void parse(OWLOntology ontology, String s) {
        SPARQLTokenizer tokenizer = new SPARQLTokenizerJavaCCImpl(ontology, new StringReader(s));
        SPARQLParserImpl parser = new SPARQLParserImpl(tokenizer);

        Query query = parser.parseQuery();

//        SelectQuery query = parser.parseQuery();

        new AlgebraPrettyPrinter().prettyPrint(query.translate(), new PrintWriter(System.out));


        Stopwatch stopwatch = Stopwatch.createStarted();
        SPARQLQueryResult result = queryEngine.ask(query.asSelectQuery());
        System.out.println("Answered query in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");

        if (query instanceof ConstructQuery) {
            ConstructQuery constructQuery = (ConstructQuery) query;
            Set<OWLAxiom> axioms = new HashSet<>();
            for (SolutionMapping sm : result.getSolutionSequence().getSolutionMappings()) {
                for(Axiom ax : constructQuery.getConstructTemplate().getAxioms()) {
                    Optional<? extends Axiom> boundAxiom = ax.bind(sm);
                    if(boundAxiom.isPresent()) {
                        Axiom boundAx = boundAxiom.get();
                        OWLDataFactory df = ontology.getOWLOntologyManager().getOWLDataFactory();
                        OWLAxiom owlAxiom = boundAx.toOWLObject(df);
                        axioms.add(owlAxiom);
                    }
                }
            }
            try {
                OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
                OWLOntology constructedOntology = manager.createOntology(axioms);
                TurtleDocumentFormat documentFormat = new TurtleDocumentFormat();
                documentFormat.copyPrefixesFrom(query.getPrefixManager());
                manager.setOntologyFormat(constructedOntology, documentFormat);
                StringWriter sw = new StringWriter();
                manager.saveOntology(constructedOntology, new WriterDocumentTarget(sw));
                JTextPane textPane = new JTextPane();
                textPane.setFont(new Font("menlo", Font.PLAIN, 12));
                textPane.setText(sw.toString());
                JScrollPane scrollPane = new JScrollPane(textPane);
                scrollPane.setSize(400, 400);
                JFrame frame = new JFrame("Constructed ontology");
                frame.setContentPane(scrollPane);
                frame.pack();
                frame.setVisible(true);
            } catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
                e.printStackTrace();
            }
        }

        SPARQLResultsTable table = new SPARQLResultsTable();
        table.setPrefixManager(query.getPrefixManager());
        JFrame f = new JFrame();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table));
        panel.add(new JLabel(result.getSize() + " result"), BorderLayout.SOUTH);
        f.setContentPane(panel);
        f.setSize(400, 400);
        f.setVisible(true);

        table.setResults(result);
    }
}
