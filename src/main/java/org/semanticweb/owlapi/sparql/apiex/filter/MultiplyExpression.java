package org.semanticweb.owlapi.sparql.apiex.filter;

import org.semanticweb.owlapi.sparql.apiex.expr.*;
import org.semanticweb.owlapi.sparql.apiex.expr.Expression;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2012
 */
public class MultiplyExpression extends ArithmeticExpression {

    public MultiplyExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Expression(MULTIPLY ");
        sb.append(getLeft());
        sb.append(", ");
        sb.append(getRight());
        sb.append(")");
        return sb.toString();
    }
}
