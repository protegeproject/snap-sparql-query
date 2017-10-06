package org.semanticweb.owlapi.sparql.api;

import com.google.common.collect.ImmutableList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class GroupClause {

    private final ImmutableList<GroupCondition> groupConditions;

    public GroupClause(ImmutableList<GroupCondition> groupConditions) {
        this.groupConditions = checkNotNull(groupConditions);
    }

    public ImmutableList<GroupCondition> getGroupConditions() {
        return groupConditions;
    }

    public ImmutableList<UntypedVariable> getGroupVariables() {
        ImmutableList.Builder<UntypedVariable> builder = ImmutableList.builder();
        for(GroupCondition condition : groupConditions) {
            condition.getGroupConditionVariable().ifPresent(builder::add);
        }
        return builder.build();
    }
}
