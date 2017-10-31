package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Oct 2017
 */
public class Cast implements Expression {

    @Nonnull
    private Datatype castTo;

    @Nonnull
    private final Expression arg;

    public Cast(@Nonnull Datatype castTo, @Nonnull Expression arg) {
        this.castTo = checkNotNull(castTo);
        this.arg = checkNotNull(arg);
    }

    @Nonnull
    public Datatype getCastTo() {
        return castTo;
    }

    @Nonnull
    public Expression getArg() {
        return arg;
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }

    @Override
    public EvaluationResult evaluate(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        EvaluationResult result = arg.evaluate(sm, evaluationContext);
        if(result.isError()) {
            return EvaluationResult.getError();
        }
        RDFTerm term = result.getResult();
        Optional<Literal> castTerm = Optional.empty();
        if(castTo.isXSDString()) {
            castTerm = term.castToXSDString();
        }
        else if(castTo.isXSDFloat()) {
            castTerm = term.castToXSDFloat();
        }
        else if(castTo.isXSDDouble()) {
            castTerm = term.castToXSDDouble();
        }
        else if(castTo.isXSDDecimal()) {
            castTerm = term.castToXSDDecimal();
        }
        else if(castTo.isXSDInteger()) {
            castTerm = term.castToXSDInteger();
        }
        else if(castTo.isXSDDateTime()) {
            castTerm = term.castToXSDDateTime();
        }
        else if(castTo.isXSDBoolean()) {
            castTerm = term.castToXSDBoolean();
        }
        return castTerm.map(EvaluationResult::new).orElse(EvaluationResult.getError());
    }

    @Override
    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    @Override
    public EvaluationResult evaluateAsLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    @Override
    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    @Override
    public Set<Variable> getVariables() {
        return Collections.emptySet();
    }
}
