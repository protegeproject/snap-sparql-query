package org.semanticweb.owlapi.sparql.apiex.expr;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2012
 */
public abstract class ArithmeticExpression implements Expression {

    private Expression left;
    
    private Expression right;

    protected ArithmeticExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isNumeric() {
        return true;
    }
}
