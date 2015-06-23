package org.semanticweb.owlapi.sparql.syntax;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.api.UntypedVariable;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class SelectClause {

    private final boolean selectDistinct;

    private final ImmutableList<SelectItem> selectItems;

    public SelectClause(boolean selectDistinct, ImmutableList<SelectItem> selectItems) {
        this.selectDistinct = selectDistinct;
        this.selectItems = selectItems;
    }

    public boolean isSelectDistinct() {
        return selectDistinct;
    }

    public ImmutableList<SelectItem> getSelectItems() {
        return selectItems;
    }

    public List<UntypedVariable> getVariables() {
        return selectItems
                .stream()
                .map(SelectItem::getVariable)
                .collect(toList());
    }

    public boolean containsAggregates() {
        return selectItems
                .stream()
                .anyMatch(SelectItem::isAggregate);
    }
}
