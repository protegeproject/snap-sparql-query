package org.semanticweb.owlapi.sparql.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public enum VariableNamePrefix {

    QUESTION_MARK("?"),

    DOLLAR("$");
    
    private String prefix;

    private VariableNamePrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public static VariableNamePrefix getDefault() {
        return QUESTION_MARK;
    }
}
