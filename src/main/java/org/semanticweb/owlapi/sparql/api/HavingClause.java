package org.semanticweb.owlapi.sparql.api;

import com.google.common.collect.ImmutableList;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 26/06/15
 */
public class HavingClause {

    private final ImmutableList<HavingCondition> conditions;


    public HavingClause(ImmutableList<HavingCondition> conditions) {
        this.conditions = conditions;
    }


    public ImmutableList<HavingCondition> getConditions() {
        return conditions;
    }
}
