package org.semanticweb.owlapi.sparql.api;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.apiex.BuiltInCall;

import java.util.*;
import java.util.regex.Pattern;

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
        StringBuilder sb = new StringBuilder();
        sb.append("Expression(BUILTIN ");
        sb.append(builtInCall.name().toUpperCase());
        sb.append(" ");
        for(Expression expression : args) {
            sb.append("Arg(");
            sb.append(expression);
            sb.append(") ");
        }
        sb.append(")");
        return sb.toString();
    }

    public BuiltInCall getBuiltInCall() {
        return builtInCall;
    }

    public List<Expression> getArgs() {
        return args;
    }

    public Set<Variable> getVariables() {
        Set<Variable> result = new HashSet<Variable>();
        for(Expression arg : args) {
            Set<Variable> argVariables = arg.getVariables();
            result.addAll(argVariables);
        }
        return result;
    }

    public EvaluationResult evaluate(SolutionMapping sm) {
        return builtInCall.getEvaluator().evaluate(args, sm);
    }

    public boolean canEvaluateAsBoolean(SolutionMapping sm) {
        return true;
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm) {
        if(!builtInCall.getResultType().isBoolean()) {
            return EvaluationResult.getError();
        }
        else {
            return builtInCall.getEvaluator().evaluate(args, sm);
        }
    }


    public boolean canEvaluateAsSimpleLiteral(SolutionMapping sm) {
        return builtInCall.getResultType().isSimpleLiteral();
    }

    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm) {
        if(!builtInCall.getResultType().isSimpleLiteral()) {
            return EvaluationResult.getError();
        }
        else {
            return builtInCall.getEvaluator().evaluate(args, sm);
        }
    }

    public boolean canEvaluateAsStringLiteral(SolutionMapping sm) {
        return builtInCall.getResultType().isStringLiteral();
    }

    public EvaluationResult evaluateAsStringLiteral(SolutionMapping sm) {
//        if(!builtInCall.getResultType().isStringLiteral()) {
//            return EvaluationResult.getError();
//        }
//        else {
            return builtInCall.getEvaluator().evaluate(args, sm);
//        }
    }

    public boolean canEvaluateAsNumeric(SolutionMapping sm) {
        return builtInCall.getResultType().isNumeric();
    }

    public EvaluationResult evaluateAsNumeric(SolutionMapping sm) {
        if(!builtInCall.getResultType().isNumeric()) {
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
        if(!builtInCall.getResultType().isLiteral()) {
            return EvaluationResult.getError();
        }
        return builtInCall.getEvaluator().evaluate(args, sm);
    }


    @Override
    public EvaluationResult evaluateAsIRI(SolutionMapping sm) {
        if(!builtInCall.getResultType().isIRI()) {
            return EvaluationResult.getError();
        }
        return builtInCall.getEvaluator().evaluate(args, sm);
    }
}
