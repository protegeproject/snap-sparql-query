package org.semanticweb.owlapi.sparql.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class SubDataPropertyOf implements Axiom, HasSubProperty<DataPropertyExpression>, HasSuperProperty<DataPropertyExpression>, HasDataPropertyExpressions {

    private DataPropertyExpression subProperty;

    private DataPropertyExpression superProperty;

    public SubDataPropertyOf(DataPropertyExpression subProperty, DataPropertyExpression superProperty) {
        this.subProperty = subProperty;
        this.superProperty = superProperty;
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public DataPropertyExpression getSubProperty() {
        return subProperty;
    }

    public DataPropertyExpression getSuperProperty() {
        return superProperty;
    }

    public Set<DataPropertyExpression> getDataProperties() {
        return new HashSet<DataPropertyExpression>(Arrays.asList(subProperty, superProperty));
    }

    @Override
    public int hashCode() {
        return SubDataPropertyOf.class.getSimpleName().hashCode() + subProperty.hashCode() * 13 + superProperty.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SubDataPropertyOf)) {
            return false;
        }
        SubDataPropertyOf other = (SubDataPropertyOf) obj;
        return this.subProperty.equals(other.subProperty) && this.superProperty.equals(other.superProperty);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        subProperty.collectVariables(variables);
        superProperty.collectVariables(variables);
    }
}
