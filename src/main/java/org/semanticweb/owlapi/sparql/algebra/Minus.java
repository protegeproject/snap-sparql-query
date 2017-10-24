package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.sparql.sparqldl.MinusEvaluator;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class Minus extends GraphPatternAlgebraExpression<SolutionSequence> {

    private GraphPatternAlgebraExpression<SolutionSequence> left;

    private GraphPatternAlgebraExpression<SolutionSequence> right;

    public Minus(GraphPatternAlgebraExpression<SolutionSequence> left, GraphPatternAlgebraExpression<SolutionSequence> right) {
        this.left = checkNotNull(left);
        this.right = checkNotNull(right);
    }

    public GraphPatternAlgebraExpression getLeft() {
        return left;
    }

    public GraphPatternAlgebraExpression getRight() {
        return right;
    }

    @Override
    public GraphPatternAlgebraExpression<SolutionSequence> getSimplified() {
        return new Minus(left.getSimplified(), right.getSimplified());
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {
        left.collectVisibleVariables(variableBuilder);
        // Right side is not visible
    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context, EvaluationContext evaluationContext) {
        SolutionSequence leftSeq = left.evaluate(context, evaluationContext);
        SolutionSequence rightSeq = right.evaluate(context, evaluationContext);
        Set<Variable> sharedVariables = left.getSharedVariables(right);
        MinusEvaluator minusEvaluator = new MinusEvaluator(leftSeq.getSolutionMappings(), rightSeq.getSolutionMappings(), sharedVariables);
        ImmutableList<SolutionMapping> minusResult = minusEvaluator.getMinus();
        return new SolutionSequence(leftSeq.getVariableList(), minusResult);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("MINUS")
                .addValue(left)
                .addValue(right)
                .toString();
    }

    @Override
    public <R, E extends Throwable> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
