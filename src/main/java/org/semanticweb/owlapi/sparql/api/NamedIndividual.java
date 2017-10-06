package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class NamedIndividual extends AbstractEntity implements Entity, AtomicIndividual {

    public NamedIndividual(IRI iri) {
        super(iri);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "NamedIndividual(<" + getIRI() + ">)";
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
    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm) {
        return EvaluationResult.getSimpleLiteral(getIRI().toString());
    }

    @Override
    public EvaluationResult evaluateAsStringLiteral(SolutionMapping sm) {
        return EvaluationResult.getSimpleLiteral(getIRI().toString());
    }

    @Override
    public EvaluationResult evaluateAsLiteral(SolutionMapping sm) {
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
