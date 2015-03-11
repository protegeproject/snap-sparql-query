package org.semanticweb.owlapi.sparql.api;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class ObjectPropertyRange implements Axiom, HasProperty<ObjectPropertyExpression>, HasRange<ClassExpression> {

    private ObjectPropertyExpression property;

    private ClassExpression range;

    public ObjectPropertyRange(ObjectPropertyExpression property, ClassExpression range) {
        this.property = property;
        this.range = range;
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public ObjectPropertyExpression getProperty() {
        return property;
    }

    public ClassExpression getRange() {
        return range;
    }

    @Override
    public int hashCode() {
        return ObjectPropertyRange.class.getSimpleName().hashCode() + property.hashCode() + range.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ObjectPropertyRange)) {
            return false;
        }
        ObjectPropertyRange other = (ObjectPropertyRange) obj;
        return this.property.equals(other.property) && this.range.equals(other.range);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        property.collectVariables(variables);
        range.collectVariables(variables);
    }
}
