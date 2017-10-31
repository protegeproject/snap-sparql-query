package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class AnonymousIndividual implements AtomicIndividual, HasIdentifier, AnnotationSubject, AnnotationValue, RDFTerm, HasAsRDFTerm {

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

    @Override
    public boolean isNumeric() {
        return false;
    }

    @Override
    public boolean isXSDDateTime() {
        return false;
    }

    @Override
    public boolean isXSDString() {
        return false;
    }

    @Override
    public boolean isStringLiteral() {
        return false;
    }

    @Override
    public boolean isSimpleLiteral() {
        return false;
    }

    @Override
    public boolean isXSDBoolean() {
        return false;
    }

    public boolean isEntityIRI() {
        return false;
    }

    public boolean isUntypedIRI() {
        return false;
    }

    public EvaluationResult evaluate(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getResult(this);
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
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
    public void collectVariables(Collection<Variable> variables) {

    }

    @Override
    public boolean isSameRDFTermAs(Term term) {
        return this.equals(term);
    }

    @Override
    public RDFTerm asRDFTerm() {
        return this;
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }

    @Override
    public Optional<AnonymousIndividual> bind(SolutionMapping sm) {
        return Optional.of(this);
    }

    @Override
    public OWLAnonymousIndividual toOWLObject(OWLDataFactory df) {
        return df.getOWLAnonymousIndividual(getIdentifier());
    }

    @Nonnull
    @Override
    public java.util.Optional<Variable> asVariable() {
        return java.util.Optional.empty();
    }

    @Override
    public Optional<Literal> castToXSDString() {
        return Optional.empty();
    }

    @Override
    public Optional<Literal> castToXSDFloat() {
        return Optional.empty();
    }

    @Override
    public Optional<Literal> castToXSDDouble() {
        return Optional.empty();
    }

    @Override
    public Optional<Literal> castToXSDDecimal() {
        return Optional.empty();
    }

    @Override
    public Optional<Literal> castToXSDInteger() {
        return Optional.empty();
    }

    @Override
    public Optional<Literal> castToXSDDateTime() {
        return Optional.empty();
    }

    @Override
    public Optional<Literal> castToXSDBoolean() {
        return Optional.empty();
    }
}
