package org.semanticweb.owlapi.sparql.syntax;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.sparql.api.Axiom;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 24/06/15
 */
public class ConstructTemplate {

    private final ImmutableList<Axiom> axioms;

    public ConstructTemplate(ImmutableList<Axiom> axioms) {
        this.axioms = axioms;
    }

    public ImmutableList<Axiom> getAxioms() {
        return axioms;
    }

    public Collection<? extends Variable> getVariables() {
        Set<Variable> variables = new HashSet<>();
        axioms.stream()
                .forEach((a) -> a.collectVariables(variables));
        return variables;
    }
}
