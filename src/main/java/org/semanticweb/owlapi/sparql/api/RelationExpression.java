package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/07/2012
 */
public class RelationExpression implements Expression {

    private Expression left;

    private Expression right;

    private Relation relation;

    public RelationExpression(@Nonnull Expression left,
                              @Nonnull Expression right,
                              @Nonnull Relation relation) {
        this.left = checkNotNull(left);
        this.right = checkNotNull(right);
        this.relation = checkNotNull(relation);
    }

    public Relation getRelation() {
        return relation;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public Set<Variable> getVariables() {
        Set<Variable> result = new HashSet<Variable>();
        result.addAll(left.getVariables());
        result.addAll(right.getVariables());
        return result;
    }

    public EvaluationResult evaluate(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return evaluateAsEffectiveBooleanValue(sm, evaluationContext);
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return relation.evaluate(left, right, sm, evaluationContext);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("RelationExpression")
                .addValue(relation)
                .addValue(left)
                .addValue(right)
                .toString();
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }
}
