package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class NamedIndividual extends AbstractEntity implements Entity, AtomicIndividual {

    public NamedIndividual(@Nonnull IRI iri) {
        super(iri);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }


    @Override
    public String toString() {
        return toStringHelper("NamedIndividual")
                .addValue(getIRI())
                .toString();
    }

    @Override
    public int hashCode() {
        return getIRI().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NamedIndividual)) {
            return false;
        }
        NamedIndividual other = (NamedIndividual) obj;
        return this.getIRI().equals(other.getIRI());
    }

    @Override
    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getSimpleLiteral(getIRI().toString());
    }

    @Override
    public EvaluationResult evaluateAsStringLiteral(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getSimpleLiteral(getIRI().toString());
    }

    @Override
    public EvaluationResult evaluateAsLiteral(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getSimpleLiteral(getIRI().toString());
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }

    @Override
    public Optional<NamedIndividual> bind(SolutionMapping sm) {
        return Optional.of(this);
    }

    @Override
    public OWLNamedIndividual toOWLObject(OWLDataFactory df) {
        return df.getOWLNamedIndividual(getIRI());
    }
}
