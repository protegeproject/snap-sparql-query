package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.IRI;

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
        return ObjectProperty.class.getSimpleName().hashCode() + getIRI().hashCode();
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
}
