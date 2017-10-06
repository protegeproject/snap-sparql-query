package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import java.util.Optional;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

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
        return Objects.hashCode(getProperty(), getSubject(), getObject());
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
        return this.getProperty().equals(other.getProperty()) && this.getSubject().equals(other.getSubject()) && this.getObject().equals(other.getObject());
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("AnnotationAssertion")
                .addValue(getProperty())
                .addValue(getSubject())
                .addValue(getObject())
                .toString();
    }

    @Override
    public Optional<AnnotationAssertion> bind(SolutionMapping sm) {
        Optional<? extends AtomicAnnotationProperty> property = getProperty().bind(sm);
        if(!property.isPresent()) {
            return Optional.empty();
        }
        Optional<? extends AnnotationSubject> subject = getSubject().bind(sm);
        if(!subject.isPresent()) {
            return Optional.empty();
        }
        Optional<? extends AnnotationValue> value = getObject().bind(sm);
        if(!value.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(
                new AnnotationAssertion(
                        property.get(),
                        subject.get(),
                        value.get()
                )
        );
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLAnnotationAssertionAxiom(
                getProperty().toOWLObject(df),
                getSubject().toOWLObject(df),
                getObject().toOWLObject(df)
        );
    }
}
