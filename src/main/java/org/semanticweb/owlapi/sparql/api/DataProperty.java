package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class DataProperty extends AbstractEntity implements DataPropertyExpression, AtomicDataProperty {

    public DataProperty(IRI iri) {
        super(iri);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return DataProperty.class.getSimpleName().hashCode() + getIRI().hashCode();
    }

    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof DataProperty)) {
            return false;
        }
        DataProperty other = (DataProperty) obj;
        return this.getIRI().equals(other.getIRI());
    }
}
