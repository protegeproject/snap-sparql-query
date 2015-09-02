package org.semanticweb.owlapi.sparql.ui;

import javax.swing.text.*;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 01/09/15
 *
 * Pinched from http://stackoverflow.com/questions/15867900/java-auto-indentation-in-jtextpane
 */
public class NewLineDocumentFilter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
        if ("\n".equals(str)) {
            str = addWhiteSpace(fb.getDocument(), offs);
        }
        super.insertString(fb, offs, str, a);
    }

    @Override
    public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
        if ("\n".equals(str)) {
            str = addWhiteSpace(fb.getDocument(), offs);
        }
        super.replace(fb, offs, length, str, a);
    }

    private String addWhiteSpace(Document doc, int offset) throws BadLocationException {
        StringBuilder whiteSpace = new StringBuilder("\n");
        Element rootElement = doc.getDefaultRootElement();
        int line = rootElement.getElementIndex(offset);
        int i = rootElement.getElement(line).getStartOffset();
        while (true) {
            String temp = doc.getText(i, 1);

            if (temp.equals(" ") || temp.equals("\t")) {
                whiteSpace.append(temp);
                i++;
            } else {
                break;
            }
        }
        return whiteSpace.toString();
    }
}
