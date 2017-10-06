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
public class BindPattern extends Pattern {

    private Expression expression;

    private Variable variable;

    public BindPattern(Expression expression, Variable variable) {
        this.expression = checkNotNull(expression);
        this.variable = checkNotNull(variable);
    }

    public Expression getExpression() {
        return expression;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public Set<Variable> getInScopeVariables() {
        return Collections.singleton(variable);
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
        return Optional.of(this);
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
