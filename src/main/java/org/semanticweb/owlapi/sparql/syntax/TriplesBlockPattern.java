package org.semanticweb.owlapi.sparql.syntax;

import java.util.Optional;
import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.algebra.Bgp;
import org.semanticweb.owlapi.sparql.algebra.GraphPatternAlgebraExpression;
import org.semanticweb.owlapi.sparql.api.Axiom;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class TriplesBlockPattern extends Pattern {

    private ImmutableList<Axiom> axioms;

    public TriplesBlockPattern(ImmutableList<Axiom> axioms) {
        this.axioms = checkNotNull(axioms);
    }

    public ImmutableList<Axiom> getAxioms() {
        return axioms;
    }

    public boolean isEmpty() {
        return axioms.isEmpty();
    }

    public Set<Variable> getVariables() {
        Set<Variable> variables = new HashSet<>();
        for(Axiom ax : axioms) {
            ax.collectVariables(variables);
        }
        return variables;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final ImmutableList.Builder<Axiom> axiomBuilder = ImmutableList.builder();

        public void add(Axiom axiom) {
            axiomBuilder.add(axiom);
        }

        public TriplesBlockPattern build() {
            return new TriplesBlockPattern(axiomBuilder.build());
        }
    }

    @Override
    public Set<Variable> getInScopeVariables() {
        return getVariables();
    }

    @Override
    public Optional<FilterPattern> asFilterPattern() {
        return Optional.empty();
    }

    @Override
    public Optional<MinusPattern> asMinusPattern() {
        return Optional.empty();
    }

    @Override
    public Optional<BindPattern> asBindPattern() {
        return Optional.empty();
    }

    @Override
    public Optional<OptionalPattern> asOptionalPattern() {
        return Optional.empty();
    }

    @Override
    public Optional<UnionPattern> asUnionPattern() {
        return Optional.empty();
    }

    @Override
    public GraphPatternAlgebraExpression translate() {
        return new Bgp(axioms);
    }
}
