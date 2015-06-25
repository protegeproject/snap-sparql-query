package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;

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

    @Override
    public Optional<ObjectPropertyRange> bind(SolutionMapping sm) {
        Optional<? extends ObjectPropertyExpression> property = this.property.bind(sm);
        if(!property.isPresent()) {
            return Optional.absent();
        }
        Optional<? extends ClassExpression> range = this.range.bind(sm);
        if(!range.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(new ObjectPropertyRange(property.get(), range.get()));
    }
}
