package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class DifferentIndividuals implements Axiom, HasIndividuals {

    private Set<AtomicIndividual> individuals;

    public DifferentIndividuals(AtomicIndividual left, AtomicIndividual right) {
        individuals = new HashSet<AtomicIndividual>(3);
        individuals.add(left);
        individuals.add(right);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public DifferentIndividuals(Set<AtomicIndividual> individuals) {
        this.individuals = new HashSet<AtomicIndividual>(individuals);
    }

    public Set<AtomicIndividual> getIndividuals() {
        return Collections.unmodifiableSet(individuals);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        for(AtomicIndividual ind : individuals) {
            ind.collectVariables(variables);
        }
    }

    @Override
    public Optional<DifferentIndividuals> bind(SolutionMapping sm) {
        Set<AtomicIndividual> boundIndividuals = new HashSet<>();
        for(AtomicIndividual individual : individuals) {
            Optional<? extends AtomicIndividual> boundIndividual = individual.bind(sm);
            if(!boundIndividual.isPresent()) {
                return Optional.empty();
            }
            else {
                boundIndividuals.add(boundIndividual.get());
            }
        }
        return Optional.of(new DifferentIndividuals(boundIndividuals));
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLDifferentIndividualsAxiom(
                individuals.stream()
                .map(i -> i.toOWLObject(df))
                .collect(Collectors.toSet())
        );
    }
}
