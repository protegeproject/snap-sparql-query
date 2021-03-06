package org.semanticweb.owlapi.sparql.algebra;

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

    public GraphPatternAlgebraExpression<SolutionSequence> getAlgebraExpression() {
        return algebraExpression;
    }

    @Override
    public <R, E extends Throwable> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
