package org.semanticweb.owlapi.sparql.api;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class SubAnnotationPropertyOf implements Axiom, HasSubProperty<AtomicAnnotationProperty>, HasSuperProperty<AtomicAnnotationProperty> {

    private AtomicAnnotationProperty subProperty;

    private AtomicAnnotationProperty superProperty;

    public SubAnnotationPropertyOf(AtomicAnnotationProperty subProperty, AtomicAnnotationProperty superProperty) {
        this.subProperty = subProperty;
        this.superProperty = superProperty;
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public AtomicAnnotationProperty getSubProperty() {
        return subProperty;
    }

    public AtomicAnnotationProperty getSuperProperty() {
        return superProperty;
    }

    @Override
    public int hashCode() {
        return SubAnnotationPropertyOf.class.getSimpleName().hashCode() + subProperty.hashCode() + 13 + superProperty.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SubAnnotationPropertyOf)) {
            return false;
        }
        SubAnnotationPropertyOf other = (SubAnnotationPropertyOf) obj;
        return this.subProperty.equals(other.subProperty) && this.superProperty.equals(other.superProperty);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        subProperty.collectVariables(variables);
        superProperty.collectVariables(variables);
    }
}
