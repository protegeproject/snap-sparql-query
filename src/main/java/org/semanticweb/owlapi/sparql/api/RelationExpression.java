package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.sparql.algebra.EvaluationContext;

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

    public EvaluationResult evaluate(SolutionMapping sm, EvaluationContext evaluationContext) {
        return evaluateAsEffectiveBooleanValue(sm, evaluationContext);
    }

    public boolean canEvaluateAsBoolean(SolutionMapping sm) {
        return true;
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, EvaluationContext evaluationContext) {
        return relation.evaluate(left, right, sm, evaluationContext);
    }

    public boolean canEvaluateAsStringLiteral(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsStringLiteral(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsSimpleLiteral(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsNumeric(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsNumeric(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsDateTime(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsDateTime(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsIRI(SolutionMapping sm) {
        return false;
    }

    @Override
    public EvaluationResult evaluateAsLiteral(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }


    @Override
    public EvaluationResult evaluateAsIRI(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getError();
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
