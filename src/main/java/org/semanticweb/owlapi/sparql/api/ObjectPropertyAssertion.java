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
public class ObjectPropertyAssertion extends AbstractAssertion<AtomicIndividual, ObjectPropertyExpression, AtomicIndividual> {

    public ObjectPropertyAssertion(ObjectPropertyExpression property,
                                   AtomicIndividual subject,
                                   AtomicIndividual object) {
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
        return getProperty().hashCode() * 17 + getSubject().hashCode() * 13 + getObject().hashCode() * 5;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ObjectPropertyAssertion)) {
            return false;
        }
        ObjectPropertyAssertion other = (ObjectPropertyAssertion) obj;
        return this.getProperty().equals(other.getProperty()) && this.getSubject().equals(other.getSubject()) && this.getObject().equals(this.getObject());
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("ObjectPropertyAssertion")
                .addValue(getProperty())
                .addValue(getSubject())
                .addValue(getObject())
                .toString();
    }

    @Override
    public Optional<ObjectPropertyAssertion> bind(SolutionMapping sm) {
        Optional<? extends ObjectPropertyExpression> property = getProperty().bind(sm);
        if(!property.isPresent()) {
            return Optional.empty();
        }
        Optional<? extends AtomicIndividual> subject = getSubject().bind(sm);
        if(!subject.isPresent()) {
            return Optional.empty();
        }
        Optional<? extends AtomicIndividual> object = getObject().bind(sm);
        if(!object.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(
                new ObjectPropertyAssertion(
                        property.get(),
                        subject.get(),
                        object.get()
                )
        );
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLObjectPropertyAssertionAxiom(
                getProperty().toOWLObject(df),
                getSubject().toOWLObject(df),
                getObject().toOWLObject(df)
        );
    }
}
