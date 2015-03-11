package org.semanticweb.owlapi.sparql.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class AnnotationAssertion extends AbstractAssertion<AnnotationSubject, AtomicAnnotationProperty, AnnotationValue> {


    private AnnotationAssertion() {
    }

    public AnnotationAssertion(AtomicAnnotationProperty property, AnnotationSubject subject, AnnotationValue object) {
        super(property, subject, object);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return AnnotationAssertion.class.getSimpleName().hashCode() + getProperty().hashCode() * 17 + getSubject().hashCode() * 13 + getObject().hashCode() * 5;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AnnotationAssertion)) {
            return false;
        }
        AnnotationAssertion other = (AnnotationAssertion) obj;
        return this.getProperty().equals(other.getProperty()) && this.getSubject().equals(other.getSubject()) && this.getObject().equals(this.getObject());
    }
}
