package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;

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


//    private final VariableNamePrefix prefix;
//
//    private final String variableName;

    public AbstractVariable(String variableName) {
        super(variableName);
//        checkNotNull(variableName);
//        if(variableName.startsWith(VariableNamePrefix.QUESTION_MARK.getPrefix())) {
//            prefix = VariableNamePrefix.QUESTION_MARK;
//            this.variableName = variableName.substring(1);
//        }
//        else if(variableName.startsWith(VariableNamePrefix.DOLLAR.getPrefix())) {
//            prefix = VariableNamePrefix.DOLLAR;
//            this.variableName = variableName.substring(1);
//        }
//        else {
//            prefix = VariableNamePrefix.getDefault();
//            this.variableName = variableName;
//        }
    }

//    public Set<Variable> getVariables() {
//        return Collections.<Variable>singleton(this);
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Expression(");
//        sb.append(getClass().getSimpleName());
//        sb.append(" ?");
//        sb.append(variableName);
//        sb.append(")");
//        return sb.toString();
//    }
//
//    public String getIdentifier() {
//        return variableName;
//    }
//
//    public String getName() {
//        return variableName;
//    }
//
//    public VariableNamePrefix getVariableNamePrefix() {
//        return prefix;
//    }


    public EvaluationResult evaluate(SolutionMapping sm) {
        Optional<Term> term = sm.getTermForVariable(this);
        if(!term.isPresent()) {
            return EvaluationResult.getError();
        }
        return term.get().evaluate(sm);
    }

//    public boolean canEvaluateAsBoolean(SolutionMapping sm) {
//        return sm.isMapped(this) && sm.getTermForVariable(this).canEvaluateAsBoolean(sm);
//    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm) {
        Optional<Term> term = sm.getTermForVariable(this);
        if(!term.isPresent()) {
            return EvaluationResult.getError();
        }
        return term.get().evaluateAsEffectiveBooleanValue(sm);
    }

//    public boolean canEvaluateAsStringLiteral(SolutionMapping sm) {
//        return sm.isMapped(this) && sm.getTermForVariable(this).canEvaluateAsStringLiteral(sm);
//    }

    public EvaluationResult evaluateAsStringLiteral(SolutionMapping sm) {
        Optional<Term> term = sm.getTermForVariable(this);
        if(!term.isPresent()) {
            return EvaluationResult.getError();
        }
        return term.get().evaluateAsStringLiteral(sm);
    }

    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm) {
        Optional<Term> term = sm.getTermForVariable(this);
        if(!term.isPresent()) {
            return EvaluationResult.getError();
        }
        return term.get().evaluateAsSimpleLiteral(sm);
    }

    public EvaluationResult evaluateAsNumeric(SolutionMapping sm) {
        Optional<Term> term = sm.getTermForVariable(this);
        if(!term.isPresent()) {
            return EvaluationResult.getError();
        }
        return term.get().evaluateAsNumeric(sm);
    }

    public EvaluationResult evaluateAsDateTime(SolutionMapping sm) {
        Optional<Term> term = sm.getTermForVariable(this);
        if(!term.isPresent()) {
            return EvaluationResult.getError();
        }
        return term.get().evaluateAsDateTime(sm);
    }

    @Override
    public EvaluationResult evaluateAsLiteral(SolutionMapping sm) {
        Optional<Term> term = sm.getTermForVariable(this);
        if(!term.isPresent()) {
            return EvaluationResult.getError();
        }
        return term.get().evaluateAsLiteral(sm);
    }

    @Override
    public EvaluationResult evaluateAsIRI(SolutionMapping sm) {
        Optional<Term> term = sm.getTermForVariable(this);
        if(!term.isPresent()) {
            return EvaluationResult.getError();
        }
        return term.get().evaluateAsIRI(sm);
    }
}
