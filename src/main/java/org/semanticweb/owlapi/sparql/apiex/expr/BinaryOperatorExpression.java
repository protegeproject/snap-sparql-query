package org.semanticweb.owlapi.sparql.apiex.expr;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2012
 */
public abstract class BinaryOperatorExpression implements Expression {

    private Expression left;
    
    private Expression right;

    public BinaryOperatorExpression(Expression left, Expression right) {
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
        return true;
    }

    public boolean isNumeric() {
        return false;
    }
}
