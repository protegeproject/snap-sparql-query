package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class GroupConditionExpressionAs extends GroupCondition {

    private final Expression expression;

    private final Optional<UntypedVariable> variable;

    public GroupConditionExpressionAs(Expression expression, Optional<UntypedVariable> variable) {
        this.expression = expression;
        this.variable = variable;
    }

    public Expression getExpression() {
        return expression;
    }

    public Optional<UntypedVariable> getVariable() {
        return variable;
    }

    @Override
    public Optional<UntypedVariable> getGroupConditionVariable() {
        return variable;
    }
}
