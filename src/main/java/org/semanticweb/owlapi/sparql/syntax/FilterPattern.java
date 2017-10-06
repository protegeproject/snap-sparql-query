package org.semanticweb.owlapi.sparql.syntax;

import java.util.Optional;
import org.semanticweb.owlapi.sparql.algebra.Empty;
import org.semanticweb.owlapi.sparql.algebra.GraphPatternAlgebraExpression;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class FilterPattern extends Pattern {

    private Expression expression;

    public FilterPattern(Expression expression) {
        this.expression = checkNotNull(expression);
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Set<Variable> getInScopeVariables() {
        return Collections.emptySet();
    }

    @Override
    public Optional<FilterPattern> asFilterPattern() {
        return Optional.of(this);
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
        return Empty.get();
    }
}
