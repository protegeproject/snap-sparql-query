package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class Union extends GraphPatternAlgebraExpression<SolutionSequence> {

    private GraphPatternAlgebraExpression<SolutionSequence> left;

    private GraphPatternAlgebraExpression<SolutionSequence> right;

    public Union(GraphPatternAlgebraExpression<SolutionSequence> left, GraphPatternAlgebraExpression<SolutionSequence> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public GraphPatternAlgebraExpression<SolutionSequence> getSimplified() {
        return new Union(left.getSimplified(), right.getSimplified());
    }

    public GraphPatternAlgebraExpression<SolutionSequence> getLeft() {
        return left;
    }

    public GraphPatternAlgebraExpression<SolutionSequence> getRight() {
        return right;
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {
        left.collectVisibleVariables(variableBuilder);
        right.collectVisibleVariables(variableBuilder);
    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        SolutionSequence leftSeq = left.evaluate(context);
        SolutionSequence rightSeq = right.evaluate(context);
        List<Variable> variables = new ArrayList<>();
        variables.addAll(leftSeq.getVariableList());
        variables.addAll(rightSeq.getVariableList());
        ImmutableList.Builder<SolutionMapping> mappings = ImmutableList.builder();
        mappings.addAll(leftSeq.getSolutionMappings());
        mappings.addAll(rightSeq.getSolutionMappings());
        return new SolutionSequence(variables, mappings.build());
    }

    @Override
    public String toString() {
        return toStringHelper("Union")
                .addValue(left)
                .addValue(right)
                .toString();
    }

    @Override
    public <R, X extends Throwable> R accept(AlgebraExpressionVisitor<R, X> visitor) throws X {
        return visitor.visit(this);
    }
}
