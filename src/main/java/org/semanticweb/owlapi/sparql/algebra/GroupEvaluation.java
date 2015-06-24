package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableMap;

import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 23/06/15
 */
public class GroupEvaluation {

    private final ImmutableMap<GroupKey, SolutionSequence> groupMap;

    public GroupEvaluation(ImmutableMap<GroupKey, SolutionSequence> groupMap) {
        this.groupMap = groupMap;
    }

    public Set<GroupKey> getGroupKeys() {
        return groupMap.keySet();
    }

    public SolutionSequence getSolutionSequence(GroupKey key) {
        return groupMap.get(key);
    }

}
