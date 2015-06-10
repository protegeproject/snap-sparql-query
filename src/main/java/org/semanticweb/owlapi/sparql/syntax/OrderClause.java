package org.semanticweb.owlapi.sparql.syntax;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.api.OrderCondition;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class OrderClause {

    private final ImmutableList<OrderCondition> orderConditions;

    public OrderClause(ImmutableList<OrderCondition> orderConditions) {
        this.orderConditions = checkNotNull(orderConditions);
    }

    public ImmutableList<OrderCondition> getOrderConditions() {
        return orderConditions;
    }
}
