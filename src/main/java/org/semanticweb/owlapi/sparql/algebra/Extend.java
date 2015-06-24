package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.sparql.api.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class Extend extends GraphPatternAlgebraExpression<SolutionSequence> {

    private GraphPatternAlgebraExpression<SolutionSequence> algebraExpression;

    private Variable variable;

    private Expression expression;

    public Extend(GraphPatternAlgebraExpression<SolutionSequence> algebraExpression, Variable variable, Expression expression) {
        this.algebraExpression = algebraExpression;
        this.variable = variable;
        this.expression = expression;
    }

    public GraphPatternAlgebraExpression getAlgebraExpression() {
        return algebraExpression;
    }

    public Expression getExpression() {
        return expression;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {
        variableBuilder.add(variable);
    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        SolutionSequence sequence = algebraExpression.evaluate(context);
        ImmutableList.Builder<SolutionMapping> extendedSequence = ImmutableList.builder();
        for(SolutionMapping sm : sequence.getSolutionMappings()) {
            EvaluationResult result = expression.evaluate(sm);
            if(!result.isError()) {
                ImmutableMap<Variable, RDFTerm> variableTermMap = sm.asMap();
                ImmutableMap<Variable, RDFTerm> extended = ImmutableMap.<Variable, RDFTerm>builder().putAll(variableTermMap).put(variable, result.getResult()).build();
                SolutionMapping extendedMapping = new SolutionMapping(extended);
                extendedSequence.add(extendedMapping);
            }
            else {
                extendedSequence.add(sm);
            }
        }
        List<Variable> extendedVariableList = new ArrayList<>(sequence.getVariableList());
        extendedVariableList.add(variable);
        return new SolutionSequence(extendedVariableList, extendedSequence.build());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("EXTEND")
                .addValue(algebraExpression)
                .add("expression", expression)
                .add("variable", variable)
                .toString();
    }

    @Override
    public GraphPatternAlgebraExpression<SolutionSequence> getSimplified() {
        return new Extend(algebraExpression.getSimplified(), variable, expression);
    }

    @Override
    public <R, E extends Throwable> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
