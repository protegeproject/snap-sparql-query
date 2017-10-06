package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class GroupConditionBuiltInCall extends GroupCondition {

    private final BuiltInCallExpression expression;

    public GroupConditionBuiltInCall(BuiltInCallExpression expression) {
        this.expression = expression;
    }

    public BuiltInCallExpression getExpression() {
        return expression;
    }

    @Override
    public Optional<UntypedVariable> getGroupConditionVariable() {
        return Optional.empty();
    }

    @Override
    public Expression asExpression() {
        return expression;
    }
}
