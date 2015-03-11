package org.semanticweb.owlapi.sparql.apiex.filter;

import org.semanticweb.owlapi.sparql.apiex.expr.*;
import org.semanticweb.owlapi.sparql.apiex.expr.Expression;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2012
 */
public class PlusExpression extends ArithmeticExpression {

    public PlusExpression(Expression left, Expression right) {
        super(left, right);
    }
}
