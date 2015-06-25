package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class Filter extends GraphPatternAlgebraExpression<SolutionSequence> {

    private final ImmutableList<Expression> expressions;

    private final GraphPatternAlgebraExpression<SolutionSequence> algebraExpression;

    public Filter(ImmutableList<Expression> expressions, GraphPatternAlgebraExpression<SolutionSequence> algebraExpression) {
        this.expressions = expressions;
        this.algebraExpression = algebraExpression;
    }

    public ImmutableList<Expression> getExpressions() {
        return expressions;
    }

    public GraphPatternAlgebraExpression getPattern() {
        return algebraExpression;
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {
        algebraExpression.collectVisibleVariables(variableBuilder);
    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        SolutionSequence sequence = algebraExpression.evaluate(context);
        List<SolutionMapping> filteredList = new ArrayList<>();
        for(SolutionMapping sm : sequence.getSolutionMappings()) {
            for(Expression expression : expressions) {
                EvaluationResult evaluate = expression.evaluateAsEffectiveBooleanValue(sm);
                if(evaluate.isTrue()) {
                    filteredList.add(sm);
                }
            }
        }
        return new SolutionSequence(sequence.getVariableList(), ImmutableList.copyOf(filteredList));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("FILTER")
                .addValue(expressions)
                .addValue(algebraExpression)
                .toString();
    }

    @Override
    public GraphPatternAlgebraExpression<SolutionSequence> getSimplified() {
        return new Filter(expressions, algebraExpression.getSimplified());
    }

    @Override
    public <R, E extends Throwable> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
