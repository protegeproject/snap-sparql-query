package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public abstract class GraphPatternAlgebraExpression extends AlgebraExpression {

    public abstract GraphPatternAlgebraExpression getSimplified();

    public abstract void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder);

    public Set<Variable> getVisibleVariables() {
        ImmutableSet.Builder<Variable> builder = ImmutableSet.builder();
        collectVisibleVariables(builder);
        return builder.build();
    }

    public Set<Variable> getSharedVariables(GraphPatternAlgebraExpression other) {
        Set<Variable> result = new HashSet<>(this.getVisibleVariables());
        result.retainAll(other.getVisibleVariables());
        return result;
    }

}
