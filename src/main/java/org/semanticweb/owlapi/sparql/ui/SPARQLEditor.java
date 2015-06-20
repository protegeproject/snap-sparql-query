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

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.sparql.api.UntypedVariable;
import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;
import org.semanticweb.owlapi.sparql.parser.tokenizer.SPARQLTerminal;
import org.semanticweb.owlapi.sparql.parser.SPARQLParserImpl;
import org.semanticweb.owlapi.sparql.parser.tokenizer.*;
import org.semanticweb.owlapi.sparql.parser.tokenizer.impl.SPARQLTokenizerJavaCCImpl;
import org.semanticweb.owlapi.sparql.syntax.SelectClause;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/03/2012
 */
public class SPARQLEditor extends JTextPane {

    public static final String COMMENT_CHARACTER = "#";

    private final Style commentStyle;

    private Style sparqlKeywordStyle;

    private Style rdfVocabularyStyle;

    private Style variableStyle;

    private Style projectedVariableStyle;

    private Style defaultStyle;

    private Style stringStyle;

    private Style builtInStyle;

    private Style fullIRIStyle;

    private Set<String> sparqlKeywords = new HashSet<String>();

    private int errorStart;

    private int errorEnd;

    private boolean validQuery = false;

    private ArrayList<ChangeListener> changeListenerList = new ArrayList<ChangeListener>();

    private OWLOntologyProvider ontologyProvider;

    private ErrorMessageProvider errorMessageProvider = new DefaultErrorMessageProvider();


    public SPARQLEditor(OWLOntologyProvider ontologyProvider) {
        this.ontologyProvider = ontologyProvider;
        AutoCompleter autoCompleter = new AutoCompleter(ontologyProvider, this);
        setFont(new Font("verdana", Font.PLAIN, 12));
        getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                performHighlighting();
            }

            public void removeUpdate(DocumentEvent e) {
                performHighlighting();
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });
        
        sparqlKeywords.add(SPARQLTerminal.AS.getImage());
        sparqlKeywords.add(SPARQLTerminal.FILTER.getImage());
        sparqlKeywords.add(SPARQLTerminal.BASE.getImage());
        sparqlKeywords.add(SPARQLTerminal.BIND.getImage());
        sparqlKeywords.add(SPARQLTerminal.ASTERISK.getImage());
        sparqlKeywords.add(SPARQLTerminal.DISTINCT.getImage());
        sparqlKeywords.add(SPARQLTerminal.DOT.getImage());
        sparqlKeywords.add(SPARQLTerminal.PREFIX.getImage());
        sparqlKeywords.add(SPARQLTerminal.REDUCED.getImage());
        sparqlKeywords.add(SPARQLTerminal.SELECT.getImage());
        sparqlKeywords.add(SPARQLTerminal.UNION.getImage());
        sparqlKeywords.add(SPARQLTerminal.WHERE.getImage());
        sparqlKeywords.add(SPARQLTerminal.GROUP.getImage());
        sparqlKeywords.add(SPARQLTerminal.ORDER.getImage());
        sparqlKeywords.add(SPARQLTerminal.BY.getImage());
        sparqlKeywords.add(SPARQLTerminal.ASC.getImage());
        sparqlKeywords.add(SPARQLTerminal.DESC.getImage());
        sparqlKeywords.add(SPARQLTerminal.MINUS_KW.getImage());
        sparqlKeywords.add(SPARQLTerminal.OPTIONAL_KW.getImage());

        StyledDocument styledDocument = getStyledDocument();

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

        fullIRIStyle = styledDocument.addStyle("iri", null);
        StyleConstants.setForeground(fullIRIStyle, new Color(150, 150, 150));

        builtInStyle = styledDocument.addStyle("builtIn", null);
        StyleConstants.setForeground(builtInStyle, new Color(90, 158, 218));

        commentStyle = styledDocument.addStyle("comment", null);
        StyleConstants.setForeground(commentStyle, new Color(200, 200, 200));

        defaultStyle = styledDocument.addStyle("default", null);
        StyleConstants.setForeground(defaultStyle, Color.BLACK);

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "toggle-comment");
        getActionMap().put("toggle-comment", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleComment();
            }
        });
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
        performHighlighting();
    }

    public boolean isValidQuery() {
        return validQuery;
    }

    private void performHighlighting() {
        validQuery = false;
        performHighlightingInSeparateThread();
        try {
            errorStart = -1;
            errorEnd = -1;
            setToolTipText("");
            OWLOntology rootOntology = ontologyProvider.getOntology();
            StringReader reader = new StringReader(getText());
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

    private void performHighlightingInSeparateThread() {
        final Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Set<String> variables = getProjectedVariableNames();
                SPARQLTokenizer tokenizer = createTokenizer();
                performHighlighting(tokenizer, variables);
            }

            private void performHighlighting(SPARQLTokenizer tokenizer, Set<String> selectVariableNames) {
                while(tokenizer.hasMoreTokens()) {
                    SPARQLToken token = tokenizer.nextToken();
                    Style tokenStyle = getTokenStyle(token, selectVariableNames);
                    TokenPosition tokenPosition = token.getTokenPosition();
                    int start = tokenPosition.getStart();
                    int end = tokenPosition.getEnd();
                    getStyledDocument().setCharacterAttributes(start, end - start, tokenStyle, true);
                }
            }
        });
        t.start();
    }

    private Set<String> getProjectedVariableNames() {
        final SPARQLParserImpl sparqlParser = new SPARQLParserImpl(createTokenizer());
        Optional<SelectClause> selectClause;
        try {
            selectClause = Optional.of(sparqlParser.parsePrologueAndSelectClause());
        } catch (Throwable e) {
            selectClause = Optional.absent();
        }
        Set<String> variables;
        if(selectClause.isPresent()) {
            variables = new HashSet<>();
            for(UntypedVariable variable : selectClause.get().getVariables()) {
                variables.add("?" + variable.getName());
            }
        }
        else {
            variables = Collections.emptySet();
        } return variables;
    }

    private SPARQLTokenizerJavaCCImpl createTokenizer() {
        OWLOntology rootOntology = ontologyProvider.getOntology();
        return new SPARQLTokenizerJavaCCImpl(rootOntology, new StringReader(getText()));
    }


    private Style getTokenStyle(SPARQLToken token, Set<String> selectVariableNames) {
        for(TokenType type : token.getTokenTypes()) {
            if(type instanceof SPARQLTerminalTokenType) {
                if(sparqlKeywords.contains(token.getImage())) {
                    return sparqlKeywordStyle;
                }

            }
            else if(type instanceof BuiltInCallTokenType) {
                BuiltInCall call = BuiltInCall.valueOf(token.getImage());
                if (call.isSupported()) {
                    return builtInStyle;
                }
            }
            else if(type instanceof OWLRDFVocabularyTokenType) {
                return rdfVocabularyStyle;
            }
            else if(type instanceof VariableTokenType) {
                if(!selectVariableNames.isEmpty()) {
                    if(selectVariableNames.contains(token.getImage())) {
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
//            else if(type instanceof UntypedIRITokenType) {
//                    return fullIRIStyle;
//            }
        }
        return defaultStyle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(errorStart != errorEnd && errorStart != -1 && errorEnd != -1) {
            try {
                Rectangle startRect = modelToView(errorStart);
                Rectangle endRect = modelToView(errorEnd);
                Color old = g.getColor();
                g.setColor(Color.RED);
                g.drawLine(startRect.x, startRect.y + startRect.height, endRect.x + endRect.width, endRect.y + endRect.height);
                g.drawLine(startRect.x, startRect.y + startRect.height + 2, endRect.x + endRect.width, endRect.y + endRect.height + 2);
                g.setColor(old);
            }
            catch (BadLocationException e) {
                e.printStackTrace();
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
                    e.printStackTrace();
                }
            }
        }
    }
}
