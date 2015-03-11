package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class NamedIndividual extends AbstractEntity implements Entity, AtomicIndividual {

    private NamedIndividual() {
    }

    public NamedIndividual(IRI iri) {
        super(iri);
    }

    public NamedIndividual(String iri) {
        super(iri);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public AnnotationSubject toAnnotationSubject() {
        return new AtomicIRI(getIRI());
    }

    @Override
    public String toString() {
        return "NamedIndividual(<" + getIRI() + ">)";
    }

    @Override
    public int hashCode() {
        return NamedIndividual.class.getSimpleName().hashCode() + getIRI().hashCode();
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
}
