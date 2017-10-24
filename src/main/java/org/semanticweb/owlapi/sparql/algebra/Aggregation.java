package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.sparql.api.BuiltInCallExpression;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Variable;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class Aggregation extends GraphPatternAlgebraExpression<ImmutableList<AggregationEvaluation>> {

    private BuiltInCallExpression expression;

    private Group algebraExpression;

    private Variable variable;

    public Aggregation(BuiltInCallExpression expression, Group algebraExpression, Variable variable) {
        this.expression = expression;
        this.algebraExpression = algebraExpression;
        this.variable = variable;
    }

    public BuiltInCallExpression getExpression() {
        return expression;
    }

    public Group getAlgebraExpression() {
        return algebraExpression;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public Aggregation getSimplified() {
        return this;
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {
        algebraExpression.collectVisibleVariables(variableBuilder);
    }

    @Override
    public ImmutableList<AggregationEvaluation> evaluate(AlgebraEvaluationContext context, EvaluationContext evaluationContext) {
        GroupEvaluation groupEvaluation = algebraExpression.evaluate(context, evaluationContext);
        ImmutableList.Builder<AggregationEvaluation> resultBuilder = ImmutableList.builder();
        for(GroupKey groupKey : groupEvaluation.getGroupKeys()) {
            SolutionSequence groupSequence = groupEvaluation.getSolutionSequence(groupKey);
            EvaluationResult result = expression.evaluateAsAggregate(groupSequence, evaluationContext);
            AggregationEvaluation aggregationEvaluation = new AggregationEvaluation(groupKey, variable, result);
            resultBuilder.add(aggregationEvaluation);
        }
        return resultBuilder.build();
    }

    @Override
    public <R, E extends Throwable> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
