package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class FunctionalObjectProperty extends ObjectPropertyCharacteristic implements Axiom {

    public FunctionalObjectProperty(ObjectPropertyExpression property) {
        super(property);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return FunctionalObjectProperty.class.getSimpleName().hashCode() + getProperty().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof FunctionalObjectProperty)) {
            return false;
        }
        FunctionalObjectProperty other = (FunctionalObjectProperty) obj;
        return this.getProperty().equals(other.getProperty());
    }

    @Override
    public Optional<FunctionalObjectProperty> bind(SolutionMapping sm) {
        Optional<? extends ObjectPropertyExpression> property = getProperty().bind(sm);
        if(!property.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(new FunctionalObjectProperty(property.get()));
    }
}
