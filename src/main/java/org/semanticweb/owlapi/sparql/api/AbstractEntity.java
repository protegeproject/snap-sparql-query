package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public abstract class AbstractEntity implements HasIRI, Term, Entity {

    private IRI iri;

    public AbstractEntity(@Nonnull IRI iri) {
        this.iri = checkNotNull(iri);
    }

    public Set<Variable> getVariables() {
        return Collections.emptySet();
    }

    public IRI getIRI() {
        return iri;
    }

    public String getPrefixedName(SPARQLPrefixManager pm) {
        return pm.getPrefixedNameOrIri(iri);
    }

    @Override
    public boolean isSameRDFTermAs(Term term) {
        if(term == this) {
            return true;
        }
        if(!(term instanceof HasIRI)) {
            return false;
        }
        HasIRI other = (HasIRI) term;
        return this.iri.equals(other.getIRI());
    }

    @Override
    public RDFTerm asRDFTerm() {
        return new AtomicIRI(iri);
    }

    @Override
    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return null;
    }

    public String getIdentifier() {
        return iri.toString();
    }

    public boolean isLiteral() {
        return false;
    }

    public boolean isEntityIRI() {
        return true;
    }

    public boolean isUntypedIRI() {
        return false;
    }

    public EvaluationResult evaluate(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getResult(this.asRDFTerm());
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getSimpleLiteral(getIRI().toString());
    }

    public EvaluationResult evaluateAsStringLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getSimpleLiteral(getIRI().toString());
    }

    @Override
    public EvaluationResult evaluateAsLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getSimpleLiteral(getIRI().toString());
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {}


    @Override
    public final AnnotationSubject toAnnotationSubject() {
        return new AtomicIRI(getIRI());
    }
    
    @Nonnull
    public java.util.Optional<Variable> asVariable() {
        return java.util.Optional.empty();
    }
}
