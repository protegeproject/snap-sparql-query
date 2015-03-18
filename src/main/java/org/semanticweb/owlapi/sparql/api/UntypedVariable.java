package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.IRI;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/07/2012
 */
public class UntypedVariable extends AbstractVariable implements AnnotationValue  {

    public UntypedVariable(String variableName) {
        super(variableName);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public PrimitiveType getType() {
        return null;
    }

    public Term getBound(IRI iri) {
        return new AtomicIRI(iri);
    }

    @Override
    public int hashCode() {
        return UntypedVariable.class.getSimpleName().hashCode() + getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof UntypedVariable)) {
            return false;
        }
        UntypedVariable other = (UntypedVariable) obj;
        return getName().equals(other.getName());
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        variables.add(this);
    }
}
