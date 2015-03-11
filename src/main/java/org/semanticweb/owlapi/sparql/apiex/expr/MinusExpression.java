package org.semanticweb.owlapi.sparql.apiex.expr;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2012
 */
public class MinusExpression extends AdditiveExpression {

    public MinusExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Expression(MINUS ");
        sb.append(getLeft());
        sb.append(", ");
        sb.append(getRight());
        sb.append(")");
        return sb.toString();
    }
}
