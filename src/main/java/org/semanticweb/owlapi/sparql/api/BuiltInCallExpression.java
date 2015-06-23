package org.semanticweb.owlapi.sparql.api;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;

import java.util.*;
import java.util.stream.Collectors;

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

    public boolean isAggregate() {
        return builtInCall.isAggregate();
    }

//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Expression(BUILTIN ");
//        sb.append(builtInCall.name().toUpperCase());
//        sb.append(" ");
//        for(Expression expression : args) {
//            sb.append("Arg(");
//            sb.append(expression);
//            sb.append(") ");
//        }
//        sb.append(")");
//        return sb.toString();
//    }

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
//        if(!builtInCall.getResultType().isStringLiteral()) {
//            return EvaluationResult.getError();
//        }
//        else {
            return builtInCall.getEvaluator().evaluate(args, sm);
//        }
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

//    @Override
//    public Expression replaceSubExpressionWith(Expression subExpression, Expression withExpression) {
//        if(subExpression.equals(this)) {
//            return withExpression;
//        }
//        List<Expression> replacementArgs = args.stream()
//                .map((a) -> a.replaceSubExpressionWith(subExpression, withExpression))
//                .collect(Collectors.toList());
//        return new BuiltInCallExpression(builtInCall, ImmutableList.copyOf(replacementArgs));
//    }

    @Override
    public List<Expression> getSubExpressions() {
        List<Expression> result = new ArrayList<>();
        result.add(this);
        for(Expression arg : args) {
            result.addAll(arg.getSubExpressions());
        }
        return result;
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }
}
