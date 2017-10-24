package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 09/06/15
 */
public class Distinct extends AlgebraExpression<SolutionSequence> {

    private AlgebraExpression<SolutionSequence> expression;

    public Distinct(AlgebraExpression<SolutionSequence> expression) {
        this.expression = expression;
    }

    public AlgebraExpression getExpression() {
        return expression;
    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context, EvaluationContext evaluationContext) {
        SolutionSequence sequence = expression.evaluate(context, evaluationContext);
        Set<SolutionMapping> distinctSolutions = new LinkedHashSet<>(sequence.getSolutionMappings());
        return new SolutionSequence(sequence.getVariableList(), ImmutableList.copyOf(distinctSolutions));
    }

    @Override
    public <R, E extends Throwable> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
