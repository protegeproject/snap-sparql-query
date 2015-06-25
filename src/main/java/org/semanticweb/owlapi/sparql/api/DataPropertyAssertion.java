package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class DataPropertyAssertion extends AbstractAssertion<AtomicIndividual, DataPropertyExpression, AtomicLiteral> {

    public DataPropertyAssertion(DataPropertyExpression property, AtomicIndividual subject, AtomicLiteral object) {
        super(property, subject, object);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
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
        if(!(obj instanceof DataPropertyAssertion)) {
            return false;
        }
        DataPropertyAssertion other = (DataPropertyAssertion) obj;
        return this.getProperty().equals(other.getProperty()) && this.getSubject().equals(other.getSubject()) && this.getObject().equals(other.getObject());
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("DataPropertyAssertion")
                .addValue(getProperty())
                .addValue(getSubject())
                .addValue(getObject())
                .toString();
    }

    @Override
    public Optional<DataPropertyAssertion> bind(SolutionMapping sm) {
        Optional<? extends DataPropertyExpression> property = getProperty().bind(sm);
        if(!property.isPresent()) {
            return Optional.absent();
        }
        Optional<? extends AtomicIndividual> subject = getSubject().bind(sm);
        if(!subject.isPresent()) {
            return Optional.absent();
        }
        Optional<? extends AtomicLiteral> object = getObject().bind(sm);
        if(!object.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(
                new DataPropertyAssertion(property.get(), subject.get(), object.get())
        );
    }
}
