package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class ObjectPropertyVariable extends AbstractVariable implements AtomicObjectProperty {

    public ObjectPropertyVariable(String variableName) {
        super(variableName);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.OBJECT_PROPERTY;
    }

    @Override
    public Term getBound(IRI iri) {
        return new ObjectProperty(iri);
    }

    @Override
    public int hashCode() {
        return ObjectPropertyVariable.class.getSimpleName().hashCode() + getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ObjectPropertyVariable)) {
            return false;
        }
        ObjectPropertyVariable other = (ObjectPropertyVariable) obj;
        return other.getName().equals(this.getName());
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        variables.add(this);
    }
}