package org.semanticweb.owlapi.sparql.algebra;

import java.io.PrintWriter;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class Sample extends AlgebraExpression<SolutionSequence> {


    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {

        return null;
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {

    }

    @Override
    public <R, E extends Exception> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return null;
    }
}
