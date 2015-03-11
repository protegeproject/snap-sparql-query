package org.semanticweb.owlapi.sparql.api;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/08/2012
 */
public class Declaration implements Axiom {

    private Atomic atomic;

    public Declaration(Atomic atomic) {
        this.atomic = atomic;
    }

    public Atomic getAtomic() {
        return atomic;
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Declaration.class.getSimpleName().hashCode() + atomic.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Declaration)) {
            return false;
        }
        Declaration other = (Declaration) obj;
        return this.atomic.equals(other.atomic);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        atomic.collectVariables(variables);
    }
}