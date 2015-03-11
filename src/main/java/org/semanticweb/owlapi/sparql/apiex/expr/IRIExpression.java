package org.semanticweb.owlapi.sparql.apiex.expr;

import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2012
 */
public class IRIExpression implements Expression {

    IRI iri;

    public IRIExpression(IRI iri) {
        this.iri = iri;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isNumeric() {
        return false;
    }
}
