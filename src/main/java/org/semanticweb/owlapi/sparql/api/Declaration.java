package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.Collection;

import static com.google.common.base.Objects.toStringHelper;

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

    public java.util.Optional<Variable> getDeclaredVariable() {
        return atomic.asVariable();
    }

    public boolean isEntityVariableDeclaration() {
        return atomic.asVariable().map(Variable::isEntityVariable).orElse(false);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return atomic.hashCode();
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


    @Override
    public String toString() {
        return toStringHelper("Declaration")
                .addValue(atomic)
                .toString();
    }

    @Override
    public Optional<Declaration> bind(SolutionMapping sm) {
        Optional<?> atomic = this.atomic.bind(sm);
        if(!atomic.isPresent()) {
            return Optional.empty();
        }
        Object o = atomic.get();
        if(!(o instanceof Atomic)) {
            return Optional.empty();
        }
        return Optional.of(new Declaration((Atomic) o));
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        if(atomic instanceof Entity) {
            return df.getOWLDeclarationAxiom(
                    ((Entity) getAtomic()).toOWLObject(df)
            );
        }
        throw new UnboundVariableTranslationException();
    }
}
