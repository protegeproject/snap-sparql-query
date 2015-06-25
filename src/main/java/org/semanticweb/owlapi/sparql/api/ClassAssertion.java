package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class ClassAssertion implements Axiom, HasClassExpression, HasIndividual {

    private ClassExpression classExpression;

    private AtomicIndividual individual;

    public ClassAssertion(ClassExpression classExpression, AtomicIndividual individual) {
        this.classExpression = classExpression;
        this.individual = individual;
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public ClassExpression getClassExpression() {
        return classExpression;
    }

    public AtomicIndividual getIndividual() {
        return individual;
    }

    @Override
    public int hashCode() {
        return ClassAssertion.class.getSimpleName().hashCode() + classExpression.hashCode();
    }

    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ClassAssertion)) {
            return false;
        }
        ClassAssertion other = (ClassAssertion) obj;
        return this.classExpression.equals(other.classExpression) && this.individual.equals(other.individual);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        classExpression.collectVariables(variables);
        individual.collectVariables(variables);
    }

    @Override
    public Optional<ClassAssertion> bind(SolutionMapping sm) {
        Optional<? extends ClassExpression> ce = classExpression.bind(sm);
        if(!ce.isPresent()) {
            return Optional.absent();
        }
        Optional<? extends AtomicIndividual> ind = individual.bind(sm);
        if(!ind.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(new ClassAssertion(ce.get(), ind.get()));
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("ClassAssertion")
                .addValue(classExpression)
                .addValue(individual)
                .toString();
    }
}
