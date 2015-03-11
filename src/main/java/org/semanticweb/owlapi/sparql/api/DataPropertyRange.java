package org.semanticweb.owlapi.sparql.api;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class DataPropertyRange implements Axiom, HasProperty<DataPropertyExpression>, HasRange<DataRange> {

    private DataPropertyExpression property;

    private DataRange dataRange;

    public DataPropertyRange(DataPropertyExpression property, DataRange dataRange) {
        this.property = property;
        this.dataRange = dataRange;
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public DataPropertyExpression getProperty() {
        return property;
    }

    public DataRange getRange() {
        return dataRange;
    }

    @Override
    public int hashCode() {
        return DataPropertyRange.class.getSimpleName().hashCode() + property.hashCode() + dataRange.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof DataPropertyRange)) {
            return false;
        }
        DataPropertyRange other = (DataPropertyRange) obj;
        return this.property.equals(other.property) && this.dataRange.equals(other.dataRange);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        property.collectVariables(variables);
        dataRange.collectVariables(variables);
    }
}
