package org.semanticweb.owlapi.sparql.parser;

import java.util.Optional;
import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.api.GroupClause;
import org.semanticweb.owlapi.sparql.api.UntypedVariable;
import org.semanticweb.owlapi.sparql.syntax.*;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class ProjectionChecker {

    private final SelectQuery selectQuery;

    public ProjectionChecker(SelectQuery selectQuery) {
        this.selectQuery = checkNotNull(selectQuery);
    }



    public Optional<ProjectionRestrictionViolation> checkSelectClause() {
        SelectClause selectClause = selectQuery.getSelectClause();
        ImmutableList<SelectItem> selectForms = selectClause.getSelectItems();
        if(selectQuery.isAggregateQuery()) {
            Optional<GroupClause> groupClause = selectQuery.getSolutionModifier().getGroupClause();
            final ImmutableList<UntypedVariable> groupVariables;

            if(groupClause.isPresent()) {
                groupVariables = groupClause.get().getGroupVariables();
            }
            else {
                groupVariables = ImmutableList.of();
            }
            for(SelectItem item : selectForms) {
                if(item.isVariable()) {
                    if (!groupVariables.contains(item.getVariable())) {
                        return Optional.<ProjectionRestrictionViolation>of(
                                new NonGroupByVariableInProjection((SelectVariable) item, groupVariables)
                        );
                    }
                }
                else if(!item.isAggregate()) {
                    return Optional.<ProjectionRestrictionViolation>of(
                            new NonAggregateExpressionInProjection((SelectExpressionAsVariable) item)
                    );
                }
            }
        }

        Set<UntypedVariable> processedVariables = new HashSet<>();
        for(SelectItem item : selectForms) {
            if(item instanceof SelectExpressionAsVariable) {
                if(processedVariables.contains(item.getVariable())) {
                    return Optional.<ProjectionRestrictionViolation>of(
                            new ReuseOfVariableInExpressionAs((SelectExpressionAsVariable) item)
                    );
                }
            }
            processedVariables.add(item.getVariable());
        }
        return Optional.empty();
    }
}
