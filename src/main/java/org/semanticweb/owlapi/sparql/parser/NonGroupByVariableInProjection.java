package org.semanticweb.owlapi.sparql.parser;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.api.UntypedVariable;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.sparql.parser.tokenizer.SPARQLParseException;
import org.semanticweb.owlapi.sparql.syntax.SelectVariable;

import static java.util.stream.Collectors.joining;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class NonGroupByVariableInProjection extends ProjectionRestrictionViolation {

    private final SelectVariable item;

    private final ImmutableList<UntypedVariable> groupByClauseVariables;

    public NonGroupByVariableInProjection(SelectVariable item, ImmutableList<UntypedVariable> groupByClauseVariables) {
        this.item = item;
        this.groupByClauseVariables = groupByClauseVariables;
    }

    public SelectVariable getSelectItem() {
        return item;
    }

    private String getPrettyPrintedGroupByVariables() {
        return groupByClauseVariables.stream()
                .map(Variable::getDisplayName)
                .collect(joining(", "));
    }

    @Override
    public SPARQLParseException getException() {
        return SPARQLParseException.getPlainException(
                "Encountered the variable " + item.getVariable().getName() + " in the SELECT AS clause.  " +
                        "Expected a variable that appears in the GROUP BY clause (one of " +
                        getPrettyPrintedGroupByVariables()
                        + ")",
                item.getStartTokenPosition()
        );
    }
}
