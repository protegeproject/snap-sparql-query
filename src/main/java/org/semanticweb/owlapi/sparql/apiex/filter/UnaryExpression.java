package org.semanticweb.owlapi.sparql.apiex.filter;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2012
 */
public abstract class UnaryExpression extends Expression {

    private Expression expression;

    protected UnaryExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}
