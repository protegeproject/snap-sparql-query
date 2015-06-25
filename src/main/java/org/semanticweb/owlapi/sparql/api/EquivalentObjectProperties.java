package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class EquivalentObjectProperties extends NaryObjectPropertyAxiom {

    public EquivalentObjectProperties(Set<ObjectPropertyExpression> propertyExpressions) {
        super(propertyExpressions);
    }

    public EquivalentObjectProperties(ObjectPropertyExpression left, ObjectPropertyExpression right) {
        super(new HashSet<ObjectPropertyExpression>(Arrays.asList(left, right)));
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return EquivalentObjectProperties.class.getSimpleName().hashCode() + getObjectPropertyExpressions().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EquivalentObjectProperties)) {
            return false;
        }
        EquivalentObjectProperties other = (EquivalentObjectProperties) obj;
        return this.getObjectPropertyExpressions().equals(other.getObjectPropertyExpressions());
    }

    @Override
    public Optional<EquivalentObjectProperties> bind(SolutionMapping sm) {
        Optional<Set<ObjectPropertyExpression>> boundPropertyExpressions = getBoundPropertyExpressions(sm);
        return Optional.of(new EquivalentObjectProperties(boundPropertyExpressions.get()));
    }
}
