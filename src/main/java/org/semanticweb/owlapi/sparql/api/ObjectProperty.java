package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class ObjectProperty extends AbstractEntity implements Entity, AtomicObjectProperty {

    public ObjectProperty(IRI iri) {
        super(iri);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "ObjectProperty(<" + getIRI() + ">)";
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
        if(!(obj instanceof ObjectProperty)) {
            return false;
        }
        ObjectProperty other = (ObjectProperty) obj;
        return this.getIRI().equals(other.getIRI());
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }

    @Override
    public Optional<ObjectProperty> bind(SolutionMapping sm) {
        return Optional.of(this);
    }

    @Override
    public OWLObjectProperty toOWLObject(OWLDataFactory df) {
        return df.getOWLObjectProperty(getIRI());
    }
}
