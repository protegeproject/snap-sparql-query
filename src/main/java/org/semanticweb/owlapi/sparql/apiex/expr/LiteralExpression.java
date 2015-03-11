package org.semanticweb.owlapi.sparql.apiex.expr;

import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2012
 */
public class LiteralExpression implements Expression {

    private OWLLiteral literal;

    public LiteralExpression(OWLLiteral literal) {
        this.literal = literal;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isNumeric() {
        return false;
    }
}
