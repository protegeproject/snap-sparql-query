package org.semanticweb.owlapi.sparql.api;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/08/2012
 */
public class AnnotationPropertyDomain implements Axiom {

    private AtomicAnnotationProperty property;

    private AtomicIRI domain;

    public AnnotationPropertyDomain(AtomicAnnotationProperty property, AtomicIRI domain) {
        this.property = property;
        this.domain = domain;
    }

    public AtomicAnnotationProperty getProperty() {
        return property;
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public AtomicIRI getDomain() {
        return domain;
    }

    @Override
    public int hashCode() {
        return AnnotationPropertyDomain.class.getSimpleName().hashCode() + property.hashCode() + domain.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AnnotationPropertyDomain)) {
            return false;
        }
        AnnotationPropertyDomain other = (AnnotationPropertyDomain) obj;
        return this.property.equals(other.property) && this.domain.equals(other.domain);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        property.collectVariables(variables);
        domain.collectVariables(variables);
    }
}
