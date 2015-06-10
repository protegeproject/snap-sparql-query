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

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.sparql.api.UntypedVariable;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;
import org.semanticweb.owlapi.sparql.parser.tokenizer.SPARQLTerminal;
import org.semanticweb.owlapi.sparql.parser.SPARQLParserImpl;
import org.semanticweb.owlapi.sparql.parser.tokenizer.*;
import org.semanticweb.owlapi.sparql.parser.tokenizer.impl.SPARQLTokenizerJavaCCImpl;
import org.semanticweb.owlapi.vocab.Namespaces;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.io.StringReader;
import java.util.*;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/03/2012
 */
public class AutoCompleter {

    public static final int DEFAULT_MAX_ENTRIES = 100;

    private JTextComponent textComponent;

    private Set<String> wordDelimeters;

    private AutoCompleterMatcher matcher;

    private JList popupList;

    private JWindow popupWindow;

    public static final int POPUP_WIDTH = 350;

    public static final int POPUP_HEIGHT = 300;

    private String lastTextUpdate = "*";

    private int maxEntries = DEFAULT_MAX_ENTRIES;

    private KeyListener keyListener = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            processKeyPressed(e);
        }

        public void keyReleased(KeyEvent e) {

            if (e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_DOWN) {
                if (popupWindow.isVisible() && !lastTextUpdate.equals(textComponent.getText())) {
                    lastTextUpdate = textComponent.getText();
                    updatePopup(getMatches());
                }
            }
        }
    };

    private ComponentAdapter componentListener = new ComponentAdapter() {
        public void componentHidden(ComponentEvent event) {
            hidePopup();
        }

        public void componentResized(ComponentEvent event) {
            hidePopup();
        }

        public void componentMoved(ComponentEvent event) {
            hidePopup();
        }
    };

    private HierarchyListener hierarchyListener = new HierarchyListener() {
        /**
         * Called when the hierarchy has been changed. To discern the actual
         * type of change, call <code>HierarchyEvent.getChangeFlags()</code>.
         * @see java.awt.event.HierarchyEvent#getChangeFlags()
         */
        public void hierarchyChanged(HierarchyEvent e) {
            if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0) {
                createPopupWindow();
                Container frame = textComponent.getTopLevelAncestor();
                if (frame != null){
                    frame.addComponentListener(componentListener);
                }
            }
        }
    };

    private MouseListener mouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                completeWithPopupSelection();
            }
        }
    };

    private FocusListener focusListener = new FocusAdapter(){
        public void focusLost(FocusEvent event) {
            hidePopup();
        }
    };


    private OWLOntologyProvider ontologyProvider;

    public AutoCompleter(OWLOntologyProvider ontologyProvider, JTextComponent tc) {
        this.ontologyProvider = ontologyProvider;
        this.textComponent = tc;

        wordDelimeters = new HashSet<String>();
        wordDelimeters.add(" ");
        wordDelimeters.add("\n");
        wordDelimeters.add("\t");
        wordDelimeters.add("\r");
        wordDelimeters.add("[");
        wordDelimeters.add("]");
        wordDelimeters.add("{");
        wordDelimeters.add("}");
        wordDelimeters.add("(");
        wordDelimeters.add(")");
        wordDelimeters.add(",");
        wordDelimeters.add("^");

//        matcher = new AutoCompleterMatcherImpl(ontology);

        popupList = new JList();
        popupList.setAutoscrolls(true);
//        popupList.setCellRenderer(owlEditorKit.getWorkspace().createOWLCellRenderer());
        popupList.addMouseListener(mouseListener);
        popupList.setRequestFocusEnabled(false);

        textComponent.addKeyListener(keyListener);

        textComponent.addHierarchyListener(hierarchyListener);

        // moving or resizing the text component or dialog closes the popup
        textComponent.addComponentListener(componentListener);

        // switching focus to another component closes the popup
        textComponent.addFocusListener(focusListener);

        createPopupWindow();
    }


    public void cancel(){
        hidePopup();
    }


    private void processKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && e.isControlDown()) {
            // Show popup
            performAutoCompletion();
        }
//        else if (e.getKeyCode() == KeyEvent.VK_TAB) {
//            e.consume();
//            performAutoCompletion();
//        }
        else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (popupWindow.isVisible()) {
                // Hide popup
                e.consume();
                hidePopup();
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (popupWindow.isVisible()) {
                // Complete
                e.consume();
                completeWithPopupSelection();
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (popupWindow.isVisible()) {
                e.consume();
                incrementSelection();
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (popupWindow.isVisible()) {
                e.consume();
                decrementSelection();
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            hidePopup();
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            hidePopup();
        }
    }


    private void completeWithPopupSelection() {
        if (popupWindow.isVisible()) {
            Object selObject = popupList.getSelectedValue();
            if (selObject != null) {
                if (selObject instanceof OWLEntity) {
                    insertWord(getInsertText(selObject));
                }
                else {
                    insertWord(getInsertText(selObject));
                }
                hidePopup();
            }
        }
    }


    private java.util.List getMatches() {
        // We need to determine if the matches should be classes, individuals etc.

        try {
            int wordIndex = getWordIndex();
            String expression = textComponent.getDocument().getText(0, wordIndex);
            // Add a bit to the end to force a parse error
            expression += "±";
            OWLOntology ontology = ontologyProvider.getOntology();
            SPARQLTokenizerJavaCCImpl tokenizer = new SPARQLTokenizerJavaCCImpl(ontology, new StringReader(expression));
            SPARQLParserImpl sparqlParser = new SPARQLParserImpl(tokenizer);
            try {
                sparqlParser.parseQuery();
            }
            catch (SPARQLParseException e) {
                String word = getWordToComplete();
                Set matches = getMatchesFromParseException(ontology, tokenizer, e);
                List<String> kwMatches = new ArrayList<String>(matches.size() + 10);
                for (Object s : matches) {
                    String objectRendering = s.toString();
                    if (isMatchWith(word, objectRendering)) {
                        kwMatches.add(objectRendering);
                    }
                }
                return kwMatches;
            }
        }
        catch (BadLocationException e) {
        }
        return Collections.EMPTY_LIST;
    }

    private boolean isMatchWith(String word, String objectRendering) {
        return objectRendering.toLowerCase().startsWith(word.toLowerCase());
    }

    private Set getMatchesFromParseException(OWLOntology ontology, SPARQLTokenizerJavaCCImpl tokenizer, SPARQLParseException e) {
        Set<String> matches = new TreeSet<String>();
        for(OWLRDFVocabulary voc : e.getExpectedVocabulary()) {
            String pn = tokenizer.getPrefixManager().getPrefixIRI(voc.getIRI());
            matches.add(pn);
        }
        for(EntityType<?> et : e.getExpectedEntityTypes()) {
            if(et == EntityType.CLASS) {
                for(OWLClass cls : ontology.getClassesInSignature(true)) {
                    String pn = tokenizer.getPrefixManager().getPrefixIRI(cls.getIRI());
                    if (pn != null) {
                        matches.add(pn);
                    }
                    else {
                        matches.add(cls.getIRI().toQuotedString());
                    }
                }
            }
            else if(et == EntityType.OBJECT_PROPERTY) {
                for(OWLObjectProperty prop : ontology.getObjectPropertiesInSignature(true)) {
                    String pn = tokenizer.getPrefixManager().getPrefixIRI(prop.getIRI());
                    if (pn != null) {
                        matches.add(pn);
                    }
                    else {
                        matches.add(prop.getIRI().toQuotedString());
                    }
                }
            }
            else if(et == EntityType.DATA_PROPERTY) {
                for(OWLDataProperty prop : ontology.getDataPropertiesInSignature(true)) {
                    String pn = tokenizer.getPrefixManager().getPrefixIRI(prop.getIRI());
                    if (pn != null) {
                        matches.add(pn);
                    }
                    else {
                        matches.add(prop.getIRI().toQuotedString());
                    }
                }
            }
            else if(et == EntityType.NAMED_INDIVIDUAL) {
                for(OWLNamedIndividual ind : ontology.getIndividualsInSignature(true)) {
                    String pn = tokenizer.getPrefixManager().getPrefixIRI(ind.getIRI());
                    if (pn != null) {
                        matches.add(pn);
                    }
                    else {
                        matches.add(ind.getIRI().toQuotedString());
                    }
                }
            }
            else if(et == EntityType.ANNOTATION_PROPERTY) {
                for(OWLAnnotationProperty prop : ontology.getAnnotationPropertiesInSignature()) {
                    String pn = tokenizer.getPrefixManager().getPrefixIRI(prop.getIRI());
                    if (pn != null) {
                        matches.add(pn);
                    }
                    else {
                        matches.add(prop.getIRI().toQuotedString());
                    }
                }
                for(IRI builtin : OWLRDFVocabulary.BUILT_IN_ANNOTATION_PROPERTY_IRIS) {
                    String pn = tokenizer.getPrefixManager().getPrefixIRI(builtin);
                    if (pn != null) {
                        matches.add(pn);
                    }
                    else {
                        matches.add(builtin.toQuotedString());
                    }
                }
            }
        }
        for(SPARQLTerminal terminal : e.getExpectedTerminals()) {
            matches.add(terminal.getImage());
        }
        
        if(e.getExpectedTokenTypes().contains(BuiltInCallTokenType.get())) {
            for(BuiltInCall builtInCall : BuiltInCall.values()) {
                if (builtInCall.isSupported()) {
                    matches.add(builtInCall.name());
                }
            }
        }

        Map<String, String> prefixNamesToPrefixes = new HashMap<String, String>();


        Set<String> prefixes = new HashSet<String>();
        prefixes.add(Namespaces.OWL.toString());
        prefixes.add(Namespaces.RDF.toString());
        prefixes.add(Namespaces.RDFS.toString());
        prefixes.add(Namespaces.XSD.toString());
        prefixes.add(Namespaces.XML.toString());
        prefixes.add(Namespaces.SKOS.toString());

        for(OWLOntology ont : ontology.getImportsClosure()) {
            for(OWLEntity entity : ont.getSignature()) {
                IRI iri = entity.getIRI();
                String prefix = null;
                String prefixName = null;
                int lastStop = iri.length();
                for(int i = iri.length() - 1; i > 0; i--) {
                    char ch = iri.charAt(i);
                    if(ch == '#' || ch == '/') {
                        if (prefix == null) {
                            CharSequence seq = iri.subSequence(0, i + 1);
                            prefix = seq.toString();
                            prefixes.add(prefix);
                            lastStop = i;
                        }
                        else if(prefixName == null) {
                            CharSequence seq = iri.subSequence(i + 1, lastStop);
                            String seqString = seq.toString();
                            if(!OWL2Datatype.XSD_DECIMAL.getPattern().matcher(seqString).matches()) {
                                prefixName = seqString;
                                if(prefixName.endsWith(".owl")) {
                                    prefixName = prefixName.substring(0, prefixName.length() - 4);
                                }
                            }
                            lastStop = i;
                        }
                    }
                }
                prefixNamesToPrefixes.put(prefixName, prefix);
            }
        }

        if(e.getExpectedTokenTypes().contains(PrologueDeclarationIRITokenType.get())) {
            for(String prefix : prefixes) {
                matches.add("<" + prefix + ">");
            }
        }
        
        if(e.getExpectedTokenTypes().contains(PrefixNameTokenType.get())) {
            for(String prefixName : prefixNamesToPrefixes.keySet()) {
                matches.add(prefixName + ":");
            }
        }
        
        

        for(TokenType tokenType : e.getExpectedTokenTypes()) {
            if(tokenType instanceof UndeclaredVariableTokenType) {
                for(Variable variable : e.getParsedVariables()) {
                    if(variable instanceof UntypedVariable) {
                        matches.add(variable.getVariableNamePrefix().getPrefix() + variable.getName());
                    }
                }
            }
            else if(tokenType instanceof DeclaredVariableTokenType) {
                DeclaredVariableTokenType declaredVariableTokenType = (DeclaredVariableTokenType) tokenType;
                for(Variable variable : e.getParsedVariables()) {
                    if(variable.getType() == declaredVariableTokenType.getPrimitiveType()) {
                        matches.add(variable.getVariableNamePrefix().getPrefix() + variable.getName());
                    }
                }
            }
        }
        return matches;
    }


    private void createPopupWindow() {
        JScrollPane sp = new JScrollPane(popupList);
        popupWindow = new JWindow((Window) SwingUtilities.getAncestorOfClass(Window.class, textComponent));
//        popupWindow.setAlwaysOnTop(true); // this doesn't appear to work with certain Windows/java combinations
        popupWindow.getContentPane().setLayout(new BorderLayout());
        popupWindow.getContentPane().add(sp, BorderLayout.CENTER);
        popupWindow.setFocusableWindowState(false);
    }


    private void performAutoCompletion() {
        java.util.List matches = getMatches();
        if (matches.size() == 1) {
            // Don't show popup
            insertWord(getInsertText(matches.iterator().next()));
        }
        else if (matches.size() > 1) {
            // Show popup
            lastTextUpdate = textComponent.getText();
            showPopup();
            updatePopup(matches);
        }
    }


    private void insertWord(String word) {
        try {
            // remove any currently selected text - this is the default behaviour
            // of the editor when typing manually
            int selStart = textComponent.getSelectionStart();
            int selEnd = textComponent.getSelectionEnd();
            int selLen = selEnd - selStart;
            if (selLen > 0){
                textComponent.getDocument().remove(selStart, selLen);
            }

            int index = getWordIndex();
            int caretIndex = textComponent.getCaretPosition();
            if (caretIndex > 0 && caretIndex > index){
                textComponent.getDocument().remove(index, caretIndex - index);
            }
            textComponent.getDocument().insertString(index, word, null);
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }
    }


    private void showPopup() {
        if (popupWindow == null) {
            createPopupWindow();
        }
        if (!popupWindow.isVisible()) {
            popupWindow.setSize(POPUP_WIDTH, POPUP_HEIGHT);
            try {
                int wordIndex = getWordIndex();
                Point p = new Point(0, 0); // default for when the doc is empty
                if (wordIndex > 0){
                    p = textComponent.modelToView(wordIndex).getLocation();
                }
                SwingUtilities.convertPointToScreen(p, textComponent);
                p.y = p.y + textComponent.getFontMetrics(textComponent.getFont()).getHeight();
                popupWindow.setLocation(p);
            }
            catch (BadLocationException e) {
                e.printStackTrace();
            }
            popupWindow.setVisible(true);
        }
    }


    private void hidePopup() {
        popupWindow.setVisible(false);
        popupList.setListData(new Object [0]);
    }


    private void updatePopup(java.util.List matches) {
        int count = matches.size();
        if(count > maxEntries) {
            count = maxEntries;
        }
        if (!matches.isEmpty()) {
            popupList.setListData(matches.subList(0, count).toArray());
        }
        else {
            popupList.setListData(matches.toArray());
        }
        popupList.setSelectedIndex(0);

        popupWindow.setSize(POPUP_WIDTH, POPUP_HEIGHT);

    }


    private void incrementSelection() {
        if (popupList.getModel().getSize() > 0) {
            int selIndex = popupList.getSelectedIndex();
            selIndex++;
            if (selIndex > popupList.getModel().getSize() - 1) {
                selIndex = 0;
            }
            popupList.setSelectedIndex(selIndex);
            popupList.scrollRectToVisible(popupList.getCellBounds(selIndex, selIndex));
        }
    }


    private void decrementSelection() {
        if (popupList.getModel().getSize() > 0) {
            int selIndex = popupList.getSelectedIndex();
            selIndex--;
            if (selIndex < 0) {
                selIndex = popupList.getModel().getSize() - 1;
            }
            popupList.setSelectedIndex(selIndex);
            popupList.scrollRectToVisible(popupList.getCellBounds(selIndex, selIndex));
        }
    }




    private int getWordIndex() {
        int index = getEscapedWordIndex();
        if (index == -1){
            index = getUnbrokenWordIndex();
        }
        return Math.max(0, index);
    }


    // determines if we are currently inside an escaped name (if there are an uneven number of escape characters)
    private int getEscapedWordIndex() {
        try {
            int caretPos = Math.max(0, getEffectiveCaretPosition() - 1);
            String expression = textComponent.getDocument().getText(0, caretPos);
            int escapeEnd = -1;
            do{
                int escapeStart = expression.indexOf("'", escapeEnd+1);
                if (escapeStart != -1){
                    escapeEnd = expression.indexOf("'", escapeStart+1);
                    if (escapeEnd == -1){
                        return escapeStart;
                    }
                }
                else{
                    return -1;
                }
            }while(true);
        }
        catch (BadLocationException e) {
        }
        return -1;
    }


    private int getUnbrokenWordIndex() {
        try {
            int caretPos = Math.max(0, getEffectiveCaretPosition() - 1);
            if (caretPos > 0){
                for (int index = caretPos; index > -1; index--) {
                    if (wordDelimeters.contains(textComponent.getDocument().getText(index, 1))) {
                        return index + 1;
                    }
                    if (index == 0) {
                        return 0;
                    }
                }
            }
        }
        catch (BadLocationException e) {
        }
        return -1;
    }


    private String getInsertText(Object o) {
//        if (o instanceof OWLObject) {
//            OWLModelManager mngr = owlEditorKit.getModelManager();
//            return mngr.getRendering((OWLObject) o);
//        }
//        else {
            return o.toString();
//        }
    }


    private String getWordToComplete() {
        try {
            int index = getWordIndex();
            int caretIndex = getEffectiveCaretPosition();
            return textComponent.getDocument().getText(index, caretIndex - index);
        }
        catch (BadLocationException e) {
            return "";
        }
    }

    // the caret pos should be read as the start of the selection if there is one
    private int getEffectiveCaretPosition(){
        int startSel = textComponent.getSelectionStart();
        if (startSel >= 0){
            return startSel;
        }
        return textComponent.getCaretPosition();
    }


    public void uninstall() {
        hidePopup();
        textComponent.removeKeyListener(keyListener);
        textComponent.removeComponentListener(componentListener);
        textComponent.removeFocusListener(focusListener);
        textComponent.removeHierarchyListener(hierarchyListener);
    }
}
