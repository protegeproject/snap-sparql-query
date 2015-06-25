package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class SameIndividual implements Axiom, HasIndividuals {

    private Set<AtomicIndividual> individuals;

    public SameIndividual(AtomicIndividual left, AtomicIndividual right) {
        individuals = new HashSet<AtomicIndividual>(2);
        individuals.add(left);
        individuals.add(right);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public SameIndividual(Set<AtomicIndividual> individuals) {
        this.individuals = new HashSet<AtomicIndividual>(individuals);
    }

    public Set<AtomicIndividual> getIndividuals() {
        return individuals;
    }

    @Override
    public int hashCode() {
        return SameIndividual.class.getSimpleName().hashCode() + individuals.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SameIndividual)) {
            return false;
        }
        SameIndividual other = (SameIndividual) obj;
        return this.individuals.equals(other.individuals);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        for(AtomicIndividual individual : individuals) {
            individual.collectVariables(variables);
        }
    }

    @Override
    public Optional<SameIndividual> bind(SolutionMapping sm) {
        Set<AtomicIndividual> boundIndividuals = new HashSet<>();
        for(AtomicIndividual individual : individuals) {
            Optional<? extends AtomicIndividual> boundIndividual = individual.bind(sm);
            if(!boundIndividual.isPresent()) {
                return Optional.absent();
            }
            else {
                boundIndividuals.add(boundIndividual.get());
            }
        }
        return Optional.of(new SameIndividual(boundIndividuals));
    }
}
