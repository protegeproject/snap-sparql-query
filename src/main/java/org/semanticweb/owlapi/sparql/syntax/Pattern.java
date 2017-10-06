package org.semanticweb.owlapi.sparql.syntax;

import java.util.Optional;
import org.semanticweb.owlapi.sparql.algebra.GraphPatternAlgebraExpression;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public abstract class Pattern {

    public abstract Set<Variable> getInScopeVariables();

//    public abstract Pattern getSimplified();

    public abstract Optional<FilterPattern> asFilterPattern();

    public abstract Optional<MinusPattern> asMinusPattern();

    public abstract Optional<BindPattern> asBindPattern();

    public abstract Optional<OptionalPattern> asOptionalPattern();

    public abstract Optional<UnionPattern> asUnionPattern();

    public abstract GraphPatternAlgebraExpression translate();
}
