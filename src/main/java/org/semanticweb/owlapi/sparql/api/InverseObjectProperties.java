package org.semanticweb.owlapi.sparql.api;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/08/2012
 */
public class InverseObjectProperties extends NaryObjectPropertyAxiom implements Axiom {

    public InverseObjectProperties(ObjectPropertyExpression left, ObjectPropertyExpression right) {
        super(left, right);
    }

    public InverseObjectProperties(Set<ObjectPropertyExpression> propertyExpressions) {
        super(propertyExpressions);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return InverseObjectProperties.class.getSimpleName().hashCode() + getObjectPropertyExpressions().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof InverseObjectProperties)) {
            return false;
        }
        InverseObjectProperties other = (InverseObjectProperties) obj;
        return this.getObjectPropertyExpressions().equals(other.getObjectPropertyExpressions());
    }
}
