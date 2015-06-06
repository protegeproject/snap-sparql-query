package org.semanticweb.owlapi.sparql.api;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/08/2012
 */
public class AnnotationPropertyRange implements Axiom {

    private AtomicAnnotationProperty property;

    private AtomicIRI range;

    public AnnotationPropertyRange(AtomicAnnotationProperty property, AtomicIRI range) {
        this.property = property;
        this.range = range;
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public AtomicAnnotationProperty getProperty() {
        return property;
    }

    public AtomicIRI getRange() {
        return range;
    }

    @Override
    public int hashCode() {
        return AnnotationPropertyRange.class.getSimpleName().hashCode() + property.hashCode() + range.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AnnotationPropertyRange)) {
            return false;
        }
        AnnotationPropertyRange other = (AnnotationPropertyRange) obj;
        return this.property.equals(other.property) && this.range.equals(other.range);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        property.collectVariables(variables);
        range.collectVariables(variables);
    }
}
