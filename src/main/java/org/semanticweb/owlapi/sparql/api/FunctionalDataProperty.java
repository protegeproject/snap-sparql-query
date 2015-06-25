package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class FunctionalDataProperty implements Axiom, HasProperty<DataPropertyExpression> {

    private DataPropertyExpression dataPropertyExpression;

    public FunctionalDataProperty(DataPropertyExpression dataPropertyExpression) {
        this.dataPropertyExpression = dataPropertyExpression;
    }

    public DataPropertyExpression getProperty() {
        return dataPropertyExpression;
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return FunctionalDataProperty.class.getSimpleName().hashCode() + getProperty().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof FunctionalDataProperty)) {
            return false;
        }
        FunctionalDataProperty other = (FunctionalDataProperty) obj;
        return this.getProperty().equals(other.getProperty());
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        dataPropertyExpression.collectVariables(variables);
    }

    @Override
    public Optional<FunctionalDataProperty> bind(SolutionMapping sm) {
        Optional<? extends DataPropertyExpression> property = dataPropertyExpression.bind(sm);
        if(!property.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(new FunctionalDataProperty(property.get()));
    }
}
