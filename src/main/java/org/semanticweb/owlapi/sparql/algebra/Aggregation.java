package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import de.derivo.sparqldlapi.Var;
import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;
import org.semanticweb.owlapi.sparql.syntax.SelectExpressionAsVariable;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

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

    @Override
    public Aggregation getSimplified() {
        return this;
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {

    }

    @Override
    public ImmutableList<AggregationEvaluation> evaluate(AlgebraEvaluationContext context) {
        GroupEvaluation groupEvaluation = algebraExpression.evaluate(context);
        ImmutableList.Builder<AggregationEvaluation> resultBuilder = ImmutableList.builder();
        for(GroupKey groupKey : groupEvaluation.getGroupKeys()) {
            SolutionSequence groupSequence = groupEvaluation.getSolutionSequence(groupKey);
            EvaluationResult result = expression.evaluateAsAggregate(groupSequence);
            AggregationEvaluation aggregationEvaluation = new AggregationEvaluation(groupKey, variable, result);
            resultBuilder.add(aggregationEvaluation);
        }
        return resultBuilder.build();
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(Aggregation ");
        writer.print(indentation + "    ");
        writer.println(expression);
        algebraExpression.prettyPrint(writer, level + 1);
        writer.print(indentation);
        writer.println(")");
    }

    @Override
    public <R, E extends Exception> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
