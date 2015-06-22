package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class GroupConditionVariable extends GroupCondition {

    private final UntypedVariable variable;

    public GroupConditionVariable(UntypedVariable variable) {
        this.variable = variable;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public Optional<UntypedVariable> getGroupConditionVariable() {
        return Optional.of(variable);
    }
}
