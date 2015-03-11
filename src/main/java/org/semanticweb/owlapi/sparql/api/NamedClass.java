package org.semanticweb.owlapi.sparql.api;


import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class NamedClass extends AbstractEntity implements Entity, AtomicClass {

    private NamedClass() {
    }

    public NamedClass(IRI iri) {
        super(iri);
    }

    public NamedClass(String iri) {
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
        return String.format("NamedClass(%s)", getIRI());
    }
}
