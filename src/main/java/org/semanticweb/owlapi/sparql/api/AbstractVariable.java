package org.semanticweb.owlapi.sparql.api;

import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public abstract class AbstractVariable extends Variable {


    private final VariableNamePrefix prefix;
    
    private final String variableName;

    public AbstractVariable(String variableName) {
        checkNotNull(variableName);
        if(variableName.startsWith(VariableNamePrefix.QUESTION_MARK.getPrefix())) {
            prefix = VariableNamePrefix.QUESTION_MARK;
            this.variableName = variableName.substring(1);
        }
        else if(variableName.startsWith(VariableNamePrefix.DOLLAR.getPrefix())) {
            prefix = VariableNamePrefix.DOLLAR;
            this.variableName = variableName.substring(1);
        }
        else {
            prefix = VariableNamePrefix.getDefault();
            this.variableName = variableName;
        }
    }

    public Set<Variable> getVariables() {
        return Collections.<Variable>singleton(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Expression(");
        sb.append(getClass().getSimpleName());
        sb.append(" ?");
        sb.append(variableName);
        sb.append(")");
        return sb.toString();
    }

    public String getIdentifier() {
        return variableName;
    }

    public String getName() {
        return variableName;
    }

    public VariableNamePrefix getVariableNamePrefix() {
        return prefix;
    }


    public EvaluationResult evaluate(SolutionMapping sm) {
        if(!sm.isMapped(this)) {
            return EvaluationResult.getError();
        }
        return sm.getTermForVariable(this).evaluate(sm);
    }

//    public boolean canEvaluateAsBoolean(SolutionMapping sm) {
//        return sm.isMapped(this) && sm.getTermForVariable(this).canEvaluateAsBoolean(sm);
//    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm) {
        if(!sm.isMapped(this)) {
            return EvaluationResult.getError();
        }
        return sm.getTermForVariable(this).evaluateAsEffectiveBooleanValue(sm);
    }

//    public boolean canEvaluateAsStringLiteral(SolutionMapping sm) {
//        return sm.isMapped(this) && sm.getTermForVariable(this).canEvaluateAsStringLiteral(sm);
//    }

    public EvaluationResult evaluateAsStringLiteral(SolutionMapping sm) {
        if(!sm.isMapped(this)) {
            return EvaluationResult.getError();
        }
        return sm.getTermForVariable(this).evaluateAsStringLiteral(sm);
    }

//    public boolean canEvaluateAsSimpleLiteral(SolutionMapping sm) {
//        return sm.isMapped(this) && sm.getTermForVariable(this).canEvaluateAsSimpleLiteral(sm);
//    }

    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm) {
        if(!sm.isMapped(this)) {
            return EvaluationResult.getError();
        }
        return sm.getTermForVariable(this).evaluateAsSimpleLiteral(sm);
    }

//    public boolean canEvaluateAsNumeric(SolutionMapping sm) {
//        return sm.isMapped(this) && sm.getTermForVariable(this).canEvaluateAsNumeric(sm);
//    }

    public EvaluationResult evaluateAsNumeric(SolutionMapping sm) {
        if(!sm.isMapped(this)) {
            return EvaluationResult.getError();
        }
        return sm.getTermForVariable(this).evaluateAsNumeric(sm);
    }

//    public boolean canEvaluateAsDateTime(SolutionMapping sm) {
//        return sm.isMapped(this) && sm.getTermForVariable(this).canEvaluateAsDateTime(sm);
//    }

    public EvaluationResult evaluateAsDateTime(SolutionMapping sm) {
        if(!sm.isMapped(this)) {
            return EvaluationResult.getError();
        }
        return sm.getTermForVariable(this).evaluateAsDateTime(sm);
    }

//    public boolean canEvaluateAsIRI(SolutionMapping sm) {
//        return sm.isMapped(this) && sm.getTermForVariable(this).canEvaluateAsIRI(sm);
//    }

//    @Override
//    public boolean canEvaluateAsLiteral(SolutionMapping sm) {
//        return sm.isMapped(this) && sm.getTermForVariable(this).canEvaluateAsLiteral(sm);
//    }

    @Override
    public EvaluationResult evaluateAsLiteral(SolutionMapping sm) {
        if(!sm.isMapped(this)) {
            return EvaluationResult.getError();
        }
        return sm.getTermForVariable(this).evaluateAsLiteral(sm);
    }

    @Override
    public EvaluationResult evaluateAsIRI(SolutionMapping sm) {
        if(!sm.isMapped(this)) {
            return EvaluationResult.getError();
        }
        return sm.getTermForVariable(this).evaluateAsIRI(sm);
    }
}
