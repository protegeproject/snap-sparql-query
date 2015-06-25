package org.semanticweb.owlapi.sparql.api;


import com.google.common.base.Objects;
import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class AnnotationProperty extends AbstractEntity implements AtomicAnnotationProperty, Entity {

    public AnnotationProperty(IRI iri) {
        super(iri);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return AnnotationProperty.class.getSimpleName().hashCode() + getIRI().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AnnotationProperty)) {
            return false;
        }
        AnnotationProperty other = (AnnotationProperty) obj;
        return this.getIRI().equals(other.getIRI());
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("AnnotationProperty")
                .addValue(getIRI())
                .toString();
    }

    @Override
    public IRI getIRI() {
        return super.getIRI();
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }

    @Override
    public Optional<AnnotationProperty> bind(SolutionMapping sm) {
        return Optional.of(this);
    }
}
