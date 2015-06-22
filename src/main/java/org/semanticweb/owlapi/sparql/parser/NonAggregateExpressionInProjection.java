package org.semanticweb.owlapi.sparql.parser;

import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;
import org.semanticweb.owlapi.sparql.parser.tokenizer.SPARQLParseException;
import org.semanticweb.owlapi.sparql.parser.tokenizer.TokenPosition;
import org.semanticweb.owlapi.sparql.syntax.SelectExpressionAsVariable;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 *
 * Describes a situation where a select query is an aggregate query (because it contains a GROUP clause or an implicit
 * GROUP BY clause) and the select clause contains a non-aggregate EXPRESSION AS form.
 */
public class NonAggregateExpressionInProjection extends ProjectionRestrictionViolation {

    private final SelectExpressionAsVariable item;

    public NonAggregateExpressionInProjection(SelectExpressionAsVariable item) {
        this.item = checkNotNull(item);
    }

    private static String getAggregateNames() {
        return stream(BuiltInCall.values())
                .filter((b) -> b.isAggregate() && b.isSupported())
                .map(Enum::name)
                .collect(joining(", "));
    }

    @Override
    public SPARQLParseException getException() {
        TokenPosition startPos = item.getStartTokenPosition();
        TokenPosition endPos = item.getEndTokenPosition();
        TokenPosition pos = new TokenPosition(startPos.getStart(), endPos.getEnd(), startPos.getLine(), startPos.getCol());
        return SPARQLParseException.getPlainException(
                String.format(
                        "Encountered a non-aggregate built-in (%s) in the SELECT AS clause.  " +
                        "Expected one of %s.",
                        item.getVariable().getDisplayName(),
                        getAggregateNames()
                ),
                pos
        );
    }
}
