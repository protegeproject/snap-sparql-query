package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public RelationExpression(Expression left, Expression right, Relation relation) {
        this.left = left;
        this.right = right;
        this.relation = relation;
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

    public EvaluationResult evaluate(SolutionMapping sm) {
        return evaluateAsEffectiveBooleanValue(sm);
    }

    public boolean canEvaluateAsBoolean(SolutionMapping sm) {
        return true;
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm) {
        return relation.evaluate(left, right, sm);
    }

    public boolean canEvaluateAsStringLiteral(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsStringLiteral(SolutionMapping sm) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsSimpleLiteral(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsNumeric(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsNumeric(SolutionMapping sm) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsDateTime(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsDateTime(SolutionMapping sm) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsIRI(SolutionMapping sm) {
        return false;
    }

    @Override
    public EvaluationResult evaluateAsLiteral(SolutionMapping sm) {
        return EvaluationResult.getError();
    }


    @Override
    public EvaluationResult evaluateAsIRI(SolutionMapping sm) {
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
