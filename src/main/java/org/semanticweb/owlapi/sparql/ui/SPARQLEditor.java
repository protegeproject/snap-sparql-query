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

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.StringUtils;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;
import org.semanticweb.owlapi.sparql.parser.SPARQLParserImpl;
import org.semanticweb.owlapi.sparql.parser.tokenizer.*;
import org.semanticweb.owlapi.sparql.parser.tokenizer.impl.SPARQLTokenizerJavaCCImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/03/2012
 */
public class SPARQLEditor extends JTextPane {

    public static final String COMMENT_CHARACTER = "#";

    private final Style commentStyle;

    private final Style sparqlKeywordStyle;

    private final Style rdfVocabularyStyle;

    private final Style variableStyle;

    private final Style projectedVariableStyle;

    private final Style defaultStyle;

    private final Style stringStyle;

    private final Style builtInStyle;

    private final ImmutableSet<String> sparqlKeywords;

    private int errorStart;

    private int errorEnd;

    private boolean validQuery = false;

    private final ArrayList<ChangeListener> changeListenerList = new ArrayList<>();

    private final OWLOntologyProvider ontologyProvider;

    private ErrorMessageProvider errorMessageProvider = new DefaultErrorMessageProvider();

    private final Logger logger = LoggerFactory.getLogger(SPARQLEditor.class);

    public static final String SAMPLE_QUERY = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "\n" +
            "SELECT ?x (STR(?lab) AS ?label) WHERE {\n" +
            "\t?x rdf:type owl:Class .\n" +
            "\tOPTIONAL {?x rdfs:label ?lab}\n" +
            "}\n" +
            "ORDER BY ?label";



    public SPARQLEditor(OWLOntologyProvider ontologyProvider) {
        this.ontologyProvider = ontologyProvider;
        AutoCompleter autoCompleter = new AutoCompleter(ontologyProvider, this);
        setFont(new Font("verdana", Font.PLAIN, 12));
        getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                highlight();
            }

            public void removeUpdate(DocumentEvent e) {
                highlight();
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });

        ImmutableSet.Builder<String> sparqlKeywords = ImmutableSet.builder();
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.AS);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.FILTER);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.BASE);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.BIND);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.ASTERISK);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.DISTINCT);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.DOT);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.PREFIX);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.REDUCED);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.SELECT);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.CONSTRUCT);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.UNION);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.WHERE);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.GROUP);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.ORDER);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.BY);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.ASC);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.DESC);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.MINUS_KW);
        addSparqlKeyword(sparqlKeywords, SPARQLTerminal.OPTIONAL_KW);
        this.sparqlKeywords = sparqlKeywords.build();


        StyledDocument styledDocument = getStyledDocument();
        ((AbstractDocument) styledDocument).setDocumentFilter(new NewLineDocumentFilter());

        sparqlKeywordStyle = styledDocument.addStyle("keyword", null);
        StyleConstants.setForeground(sparqlKeywordStyle, new Color(170, 13, 145));

        rdfVocabularyStyle = styledDocument.addStyle("voc", null);
        StyleConstants.setForeground(rdfVocabularyStyle, new Color(63, 110, 116));

        variableStyle = styledDocument.addStyle("vs", null);
        StyleConstants.setForeground(variableStyle, new Color(28, 0, 207));

        projectedVariableStyle = styledDocument.addStyle("rvs", null);
        StyleConstants.setForeground(projectedVariableStyle, new Color(28, 0, 207));
        StyleConstants.setBold(projectedVariableStyle, true);

        stringStyle = styledDocument.addStyle("string", null);
        StyleConstants.setForeground(stringStyle, new Color(196, 26, 22));

        builtInStyle = styledDocument.addStyle("builtIn", null);
        StyleConstants.setForeground(builtInStyle, new Color(90, 158, 218));

        commentStyle = styledDocument.addStyle("comment", null);
        StyleConstants.setForeground(commentStyle, new Color(100, 100, 100));
//        StyleConstants.setForeground(commentStyle, new Color(200, 200, 200));

        defaultStyle = styledDocument.addStyle("default", null);
        StyleConstants.setForeground(defaultStyle, Color.BLACK);


        JPopupMenu popup = new JPopupMenu();
        final String toggleCommentKey = "toggle-comment";
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), toggleCommentKey);
        AbstractAction toggleCommentAction = new AbstractAction("Toggle comment") {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleComment();
            }
        };
        getActionMap().put(toggleCommentKey, toggleCommentAction);
        popup.add(toggleCommentAction);
        popup.addSeparator();
        popup.add(new AbstractAction("Copy as Rich Text") {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyRichTextToClipboard();
            }
        });
        setComponentPopupMenu(popup);

    }

    private static void addSparqlKeyword(ImmutableSet.Builder<String> sparqlKeywords, SPARQLTerminal terminal) {
        String img = terminal.getImage();
        sparqlKeywords.add(img);
        sparqlKeywords.add(img.toLowerCase());
        sparqlKeywords.add(StringUtils.capitalize(img.toLowerCase()));
    }

    private void copyRichTextToClipboard() {
        RTFEditorKit editorKit = new RTFEditorKit();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            editorKit.write(baos, getDocument(), 0, getDocument().getLength());
            DataHandler dataHandler = new DataHandler(
                    new DataInputStream(new ByteArrayInputStream(baos.toByteArray())),
                    editorKit.getContentType());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                    dataHandler,null);
        } catch (IOException | BadLocationException ex) {
            logger.warn("A problem occurred when copying text to the clipboard: {}", ex.getMessage(), ex);
        }
    }

    @Override
    public void copy() {
        copyRichTextToClipboard();
    }

    public void insertSampleQuery() {
        setText(SAMPLE_QUERY);
    }

    public void setErrorMessageProvider(ErrorMessageProvider errorMessageProvider) {
        this.errorMessageProvider = errorMessageProvider;
    }

    public ErrorMessageProvider getErrorMessageProvider() {
        return errorMessageProvider;
    }

    public void addChangeListener(ChangeListener changeListener) {
        changeListenerList.add(changeListener);
    }

    private void fireChange() {
        for(ChangeListener listener : changeListenerList) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    public void refresh() {
        highlight();
    }

    public boolean isValidQuery() {
        return validQuery;
    }

    private void highlight() {
        validQuery = false;
        performHighlightingInSeparateThread();
        try {
            errorStart = -1;
            errorEnd = -1;
            setToolTipText("");
            OWLOntology rootOntology = ontologyProvider.getOntology();
            StringReader reader = new StringReader(getTextFromDocument());
            SPARQLTokenizer tokenizer = new SPARQLTokenizerJavaCCImpl(rootOntology, reader);
            SPARQLParserImpl parser = new SPARQLParserImpl(tokenizer);
            parser.parseQuery();
            repaint();
            validQuery = true;
            fireChange();
        }
        catch (SPARQLParseException e) {
            validQuery = false;
            setToolTipText(errorMessageProvider.getErrorMessage(e));
            SPARQLToken token = e.getToken();
            TokenPosition tokenPosition = token.getTokenPosition();
            errorStart = tokenPosition.getStart();
            errorEnd = tokenPosition.getEnd();
            repaint();
        }
    }

    private String getTextFromDocument() {
        try {
            return getDocument().getText(0, getDocument().getLength());
        } catch (BadLocationException e) {
            logger.warn("BadLocationException when retrieving text from document");
            throw new RuntimeException(e);
        }
    }

    private void performHighlightingInSeparateThread() {
        final Thread t = new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // Not critical
            }
            performHighlighting();
        });
        t.start();
    }

    private void performHighlighting() {
        Set<String> projectedVariableNames = getProjectedVariableNames();
        SPARQLTokenizer tokenizer = createTokenizer();
        while(tokenizer.hasMoreTokens()) {
            SPARQLToken token = tokenizer.nextToken();
            Style tokenStyle = getTokenStyle(token, projectedVariableNames);
            TokenPosition tokenPosition = token.getTokenPosition();
            int start = tokenPosition.getStart();
            int end = tokenPosition.getEnd();
            getStyledDocument().setCharacterAttributes(start, end - start, tokenStyle, true);
        }
    }

    private Set<String> getProjectedVariableNames() {
        return parseProjectedVariables()
            .stream()
                .map((v) -> "?" + v.getName())
                .collect(Collectors.toSet());
    }

    private Collection<? extends Variable> parseProjectedVariables() {
        try {
            SPARQLParserImpl parser = new SPARQLParserImpl(createTokenizer());
            return parser.parseProjectedVariables();
        } catch (Throwable e) {
            return Collections.emptySet();
        }
    }

    private SPARQLTokenizerJavaCCImpl createTokenizer() {
        OWLOntology rootOntology = ontologyProvider.getOntology();
        return new SPARQLTokenizerJavaCCImpl(rootOntology, new StringReader(getTextFromDocument()));
    }


    private Style getTokenStyle(SPARQLToken token, Set<String> projectedVariable) {
        for(TokenType type : token.getTokenTypes()) {
            if(type instanceof SPARQLTerminalTokenType) {
                if(sparqlKeywords.contains(token.getImage())) {
                    return sparqlKeywordStyle;
                }

            }
            else if(type instanceof BuiltInCallTokenType) {
                BuiltInCall call = BuiltInCall.valueOf(token.getImage().toUpperCase());
                if (call.isSupported()) {
                    return builtInStyle;
                }
            }
            else if(type instanceof ScalarKeyTokenType) {
                return builtInStyle;
            }
            else if(type instanceof OWLRDFVocabularyTokenType) {
                return rdfVocabularyStyle;
            }
            else if(type instanceof VariableTokenType) {
                if(!projectedVariable.isEmpty()) {
                    if(projectedVariable.contains(token.getImage())) {
                        return projectedVariableStyle;
                    }
                }
                else {
                    return projectedVariableStyle;
                }
                return variableStyle;

            }
            else if(type instanceof StringTokenType) {
                return stringStyle;
            }
            else if(type instanceof CommentTokenType) {
                return commentStyle;
            }
        }
        return defaultStyle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(errorStart < errorEnd && errorStart != -1 && errorEnd != -1) {
            try {
                Rectangle startRect = modelToView(errorStart);
                Rectangle endRect = modelToView(errorEnd);
                if (startRect != null && endRect != null) {
                    Color old = g.getColor();
                    g.setColor(Color.RED);
                    g.drawLine(startRect.x, startRect.y + startRect.height, endRect.x + endRect.width, endRect.y + endRect.height);
                    g.drawLine(startRect.x, startRect.y + startRect.height + 2, endRect.x + endRect.width, endRect.y + endRect.height + 2);
                    g.setColor(old);
                }
            }
            catch (BadLocationException e) {
                logger.warn("BadLocationException when painting error marker. ErrorMarkerStart: {}, ErrorMarkerEnd: {}", errorStart, errorEnd);
            }
        }
    }

    private void toggleComment() {
        int caretPos = getCaretPosition();
        if(caretPos == -1) {
            return;
        }
        for(int i = caretPos - 1; i > -1; i--) {
            Document doc = getDocument();
            if (doc.getLength() > i) {
                try {
                    String text = doc.getText(i, 1);
                    if(text.equals("\n")) {
                        String nextChar = doc.getText(i + 1, 1);
                        if(nextChar.equals(COMMENT_CHARACTER)) {
                            doc.remove(i + 1, 1);
                        }
                        else {
                            doc.insertString(i + 1, COMMENT_CHARACTER, commentStyle);
                        }
                        break;
                    }
                } catch (BadLocationException e) {
                    logger.warn("BadLocationException when toggling comment. Start: {} Length: {}: ", i + 1, 1);
                }
            }
        }
    }
}
