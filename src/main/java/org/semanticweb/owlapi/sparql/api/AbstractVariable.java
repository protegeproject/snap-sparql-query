package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import java.util.Optional;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public abstract class AbstractVariable extends Variable {

    public AbstractVariable(String variableName) {
        super(variableName);
    }


    @Override
    public boolean isSameRDFTermAs(Term term) {
        if(term == this) {
            return true;
        }
        if(!(term instanceof AbstractVariable)) {
            return false;
        }
        AbstractVariable other = (AbstractVariable) term;
        return this.getName().equals(other.getName());
    }

    public AnnotationSubject toAnnotationSubject() {
        return new IRIVariable(getName());
    }

    public EvaluationResult evaluate(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        Optional<RDFTerm> term = sm.getTermForVariable(this);
        if(!term.isPresent()) {
            return EvaluationResult.getError();
        }
        return term.get().evaluate(sm, evaluationContext);
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        Optional<RDFTerm> term = sm.getTermForVariable(this);
        if(!term.isPresent()) {
            return EvaluationResult.getError();
        }
        return term.get().evaluateAsEffectiveBooleanValue(sm, evaluationContext);
    }

    @Nonnull
    @Override
    public final java.util.Optional<Variable> asVariable() {
        return java.util.Optional.of(this);
    }
}
