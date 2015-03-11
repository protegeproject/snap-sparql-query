package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.IRI;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class AtomicIRI implements Atomic, HasIRI, AnnotationSubject, AnnotationValue {

    private String iri;

    public AtomicIRI(IRI iri) {
        this.iri = iri.toString();
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public AtomicIRI(String iri) {
        this.iri = iri;
    }

    public Set<Variable> getVariables() {
        return Collections.emptySet();
    }

    public String getIRI() {
        return iri;
    }

    public String getIdentifier() {
        return iri;
    }

    public EvaluationResult evaluate(SolutionMapping sm) {
        return EvaluationResult.getResult(this);
    }

    public boolean canEvaluateAsStringLiteral(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsStringLiteral(SolutionMapping sm) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsBoolean(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm) {
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

    public boolean canEvaluateAsSimpleLiteral(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm) {
        return EvaluationResult.getError();
    }

    @Override
    public int hashCode() {
        return AtomicIRI.class.getSimpleName().hashCode() + iri.hashCode();
    }

    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AtomicIRI)) {
            return false;
        }
        AtomicIRI other = (AtomicIRI) obj;
        return this.iri.equals(other.iri);
    }

    public boolean isLiteral() {
        return false;
    }

    public boolean isEntityIRI() {
        return false;
    }

    public boolean isUntypedIRI() {
        return true;
    }

    @Override
    public EvaluationResult evaluateAsLiteral(SolutionMapping sm) {
        return EvaluationResult.getError();
    }

    @Override
    public EvaluationResult evaluateAsIRI(SolutionMapping sm) {
        return EvaluationResult.getResult(this);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {

    }
}
