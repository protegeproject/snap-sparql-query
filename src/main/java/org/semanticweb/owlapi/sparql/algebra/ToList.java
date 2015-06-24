package org.semanticweb.owlapi.sparql.algebra;

import java.io.PrintWriter;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 09/06/15
 */
public class ToList extends AlgebraExpression<SolutionSequence> {

    private GraphPatternAlgebraExpression<SolutionSequence> algebraExpression;

    public ToList(GraphPatternAlgebraExpression<SolutionSequence> algebraExpression) {
        this.algebraExpression = algebraExpression;
    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        return algebraExpression.evaluate(context);
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(ToList ");
        algebraExpression.prettyPrint(writer, level + 1);
        writer.print(indentation);
        writer.println(")");
    }


    @Override
    public <R, E extends Exception> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
