package org.semanticweb.owlapi.sparql.api;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.algebra.SolutionSequence;
import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;
import org.semanticweb.owlapi.sparql.builtin.eval.BuiltInAggregateCallEvaluator;
import org.semanticweb.owlapi.sparql.builtin.eval.BuiltInCallEvaluator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2012
 */
public class BuiltInCallExpression implements Expression {

    private final BuiltInCall builtInCall;

    private final ImmutableList<Expression> args;

    public BuiltInCallExpression(BuiltInCall builtInCall, ImmutableList<Expression> args) {
        this.builtInCall = builtInCall;
        this.args = args;
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

    public EvaluationResult evaluate(SolutionMapping sm) {
        return builtInCall.getEvaluator().evaluate(args, sm);
    }

    public EvaluationResult evaluateAsAggregate(SolutionSequence solutionSequence) {
        BuiltInCallEvaluator evaluator = builtInCall.getEvaluator();
        if(!(evaluator instanceof BuiltInAggregateCallEvaluator)) {
            throw new RuntimeException("Expected an aggregate builtin call evaluator");
        }
        return ((BuiltInAggregateCallEvaluator) evaluator).evaluateAsAggregate(args, solutionSequence);
    }

    public boolean canEvaluateAsBoolean(SolutionMapping sm) {
        return true;
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm) {
        if(!builtInCall.getReturnType().isBoolean()) {
            return EvaluationResult.getError();
        }
        else {
            return builtInCall.getEvaluator().evaluate(args, sm);
        }
    }


    public boolean canEvaluateAsSimpleLiteral(SolutionMapping sm) {
        return builtInCall.getReturnType().isSimpleLiteral();
    }

    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm) {
        if(!builtInCall.getReturnType().isSimpleLiteral()) {
            return EvaluationResult.getError();
        }
        else {
            return builtInCall.getEvaluator().evaluate(args, sm);
        }
    }

    public boolean canEvaluateAsStringLiteral(SolutionMapping sm) {
        return builtInCall.getReturnType().isStringLiteral();
    }

    public EvaluationResult evaluateAsStringLiteral(SolutionMapping sm) {
        return builtInCall.getEvaluator().evaluate(args, sm);
    }

    public boolean canEvaluateAsNumeric(SolutionMapping sm) {
        return builtInCall.getReturnType().isNumeric();
    }

    public EvaluationResult evaluateAsNumeric(SolutionMapping sm) {
        if(!builtInCall.getReturnType().isNumeric()) {
            return EvaluationResult.getError();
        }
        else {
            return builtInCall.getEvaluator().evaluate(args, sm);
        }
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
        if(!builtInCall.getReturnType().isLiteral()) {
            return EvaluationResult.getError();
        }
        return builtInCall.getEvaluator().evaluate(args, sm);
    }


    @Override
    public EvaluationResult evaluateAsIRI(SolutionMapping sm) {
        if(!builtInCall.getReturnType().isIRI()) {
            return EvaluationResult.getError();
        }
        return builtInCall.getEvaluator().evaluate(args, sm);
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }
}
