package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;

import java.util.Collection;

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
}
