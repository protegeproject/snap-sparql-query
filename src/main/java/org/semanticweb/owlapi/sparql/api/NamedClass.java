package org.semanticweb.owlapi.sparql.api;


import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.annotation.Nonnull;
import java.util.Optional;

import org.semanticweb.owlapi.model.IRI;

import static com.google.common.base.MoreObjects.toStringHelper;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class NamedClass extends AbstractEntity implements Entity, AtomicClass {

    public NamedClass(@Nonnull IRI iri) {
        super(iri);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return getIRI().hashCode() * 17;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NamedClass)) {
            return false;
        }
        NamedClass other = (NamedClass) obj;
        return other.getIRI().equals(this.getIRI());
    }


    @Override
    public String toString() {
        return toStringHelper("NamedClass")
                .addValue(getIRI())
                .toString();
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }

    @Override
    public Optional<NamedClass> bind(SolutionMapping sm) {
        return Optional.of(this);
    }

    @Override
    public OWLClass toOWLObject(OWLDataFactory df) {
        return df.getOWLClass(getIRI());
    }

    @Nonnull
    @Override
    public java.util.Optional<Variable> asVariable() {
        return java.util.Optional.empty();
    }
}
