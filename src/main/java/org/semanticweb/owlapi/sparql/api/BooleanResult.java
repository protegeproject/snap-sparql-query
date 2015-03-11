package org.semanticweb.owlapi.sparql.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/07/2012
 */
public enum BooleanResult {

    TRUE,

    FALSE,

    ERROR;

    public boolean toBoolean() {
        return this == TRUE;
    }

    public boolean isError() {
        return this == ERROR;
    }


}
