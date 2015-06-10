package org.semanticweb.owlapi.sparql.syntax;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.sparql.algebra.GraphPatternAlgebraExpression;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class OptionalPattern extends Pattern {

    private final GroupPattern groupPattern;

    public OptionalPattern(GroupPattern groupPattern) {
        this.groupPattern = checkNotNull(groupPattern);
    }

    public GroupPattern getGroupPattern() {
        return groupPattern;
    }

    @Override
    public Set<Variable> getInScopeVariables() {
        return groupPattern.getVariables();
    }

    @Override
    public Optional<FilterPattern> asFilterPattern() {
        return Optional.absent();
    }

    @Override
    public Optional<MinusPattern> asMinusPattern() {
        return Optional.absent();
    }

    @Override
    public Optional<BindPattern> asBindPattern() {
        return Optional.absent();
    }

    @Override
    public Optional<OptionalPattern> asOptionalPattern() {
        return Optional.of(this);
    }

    @Override
    public Optional<UnionPattern> asUnionPattern() {
        return Optional.absent();
    }

    @Override
    public GraphPatternAlgebraExpression translate() {
        return groupPattern.translate();
    }
}
