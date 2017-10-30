package org.semanticweb.owlapi.sparql.api;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.algebra.SolutionSequence;
import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;
import org.semanticweb.owlapi.sparql.builtin.eval.BuiltInAggregateCallEvaluator;
import org.semanticweb.owlapi.sparql.builtin.eval.BuiltInCallEvaluator;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2012
 */
public class BuiltInCallExpression implements Expression {

    private final BuiltInCall builtInCall;

    private final ImmutableList<Expression> args;

    public BuiltInCallExpression(@Nonnull BuiltInCall builtInCall,
                                 @Nonnull ImmutableList<Expression> args) {
        this.builtInCall = checkNotNull(builtInCall);
        this.args = checkNotNull(args);
    }


    @Override
    public String toString() {
        return toStringHelper("BuiltIn")
                .addValue(builtInCall.name())
                .addValue(args)
                .toString();
    }

    /**
     * Determines if this built in call is an aggregate call
     * @return true if this call is an aggregate call, otherwise false.
     */
    public boolean isAggregate() {
        return builtInCall.isAggregate();
    }

    public BuiltInCall getBuiltInCall() {
        return builtInCall;
    }

    public List<Expression> getArgs() {
        return args;
    }

    public Set<Variable> getVariables() {
        Set<Variable> result = new HashSet<>();
        for(Expression arg : args) {
            Set<Variable> argVariables = arg.getVariables();
            result.addAll(argVariables);
        }
        return result;
    }

    public EvaluationResult evaluate(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return builtInCall.getEvaluator().evaluate(args, sm, evaluationContext);
    }

    public EvaluationResult evaluateAsAggregate(SolutionSequence solutionSequence, AlgebraEvaluationContext evaluationContext) {
        BuiltInCallEvaluator evaluator = builtInCall.getEvaluator();
        if(!(evaluator instanceof BuiltInAggregateCallEvaluator)) {
            throw new RuntimeException("Expected an aggregate builtin call evaluator");
        }
        return ((BuiltInAggregateCallEvaluator) evaluator).evaluateAsAggregate(args, solutionSequence, evaluationContext);
    }

    public boolean canEvaluateAsBoolean(SolutionMapping sm) {
        return true;
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if(!builtInCall.getReturnType().isBoolean()) {
            return EvaluationResult.getError();
        }
        else {
            return builtInCall.getEvaluator().evaluate(args, sm, evaluationContext);
        }
    }


    public boolean canEvaluateAsSimpleLiteral(SolutionMapping sm) {
        return builtInCall.getReturnType().isSimpleLiteral();
    }

    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if(!builtInCall.getReturnType().isSimpleLiteral()) {
            return EvaluationResult.getError();
        }
        else {
            return builtInCall.getEvaluator().evaluate(args, sm, evaluationContext);
        }
    }

    public boolean canEvaluateAsStringLiteral(SolutionMapping sm) {
        return builtInCall.getReturnType().isStringLiteral();
    }

    public EvaluationResult evaluateAsStringLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return builtInCall.getEvaluator().evaluate(args, sm, evaluationContext);
    }

    public boolean canEvaluateAsNumeric(SolutionMapping sm) {
        return builtInCall.getReturnType().isNumeric();
    }

    public EvaluationResult evaluateAsNumeric(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if(!builtInCall.getReturnType().isNumeric()) {
            return EvaluationResult.getError();
        }
        else {
            return builtInCall.getEvaluator().evaluate(args, sm, evaluationContext);
        }
    }

    public boolean canEvaluateAsDateTime(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsDateTime(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if(!builtInCall.getReturnType().isDateTime()) {
            return EvaluationResult.getError();
        }
        return builtInCall.getEvaluator().evaluate(args, sm, evaluationContext);
    }

    public boolean canEvaluateAsIRI(SolutionMapping sm) {
        return false;
    }

    @Override
    public EvaluationResult evaluateAsLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if(!builtInCall.getReturnType().isLiteral()) {
            return EvaluationResult.getError();
        }
        return builtInCall.getEvaluator().evaluate(args, sm, evaluationContext);
    }


    @Override
    public EvaluationResult evaluateAsIRI(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if(!builtInCall.getReturnType().isIRI()) {
            return EvaluationResult.getError();
        }
        return builtInCall.getEvaluator().evaluate(args, sm, evaluationContext);
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }
}
