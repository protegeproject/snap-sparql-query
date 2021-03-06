package org.semanticweb.owlapi.sparql.algebra;

import java.io.PrintWriter;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 10/06/15
 */
public abstract class BinaryAlgebraExpression extends AlgebraExpression {

    private AlgebraExpression left;

    private AlgebraExpression right;

    public BinaryAlgebraExpression(AlgebraExpression left, AlgebraExpression right) {
        this.left = left;
        this.right = right;
    }

    public AlgebraExpression getLeft() {
        return left;
    }

    public AlgebraExpression getRight() {
        return right;
    }

    protected abstract String getName();
}
