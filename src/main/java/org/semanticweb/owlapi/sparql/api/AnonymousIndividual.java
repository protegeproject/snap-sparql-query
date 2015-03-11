package org.semanticweb.owlapi.sparql.api;

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
public class AnonymousIndividual implements AtomicIndividual, HasIdentifier, AnnotationSubject, AnnotationValue {

    private String id;

    public AnonymousIndividual(String id) {
        this.id = id;
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public AnnotationSubject toAnnotationSubject() {
        return this;
    }

    public Set<Variable> getVariables() {
        return Collections.emptySet();
    }

    public String getIdentifier() {
        return id;
    }

    public boolean isLiteral() {
        return false;
    }

    public boolean isEntityIRI() {
        return false;
    }

    public boolean isUntypedIRI() {
        return false;
    }

    public EvaluationResult evaluate(SolutionMapping sm) {
        return EvaluationResult.getResult(this);
    }

    public boolean canEvaluateAsBoolean(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsStringLiteral(SolutionMapping sm) {
        return true;
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
    public int hashCode() {
        return AnonymousIndividual.class.getSimpleName().hashCode() + id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AnonymousIndividual)) {
            return false;
        }
        AnonymousIndividual other = (AnonymousIndividual) obj;
        return this.id.equals(other.id);
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
    public void collectVariables(Collection<Variable> variables) {

    }
}
