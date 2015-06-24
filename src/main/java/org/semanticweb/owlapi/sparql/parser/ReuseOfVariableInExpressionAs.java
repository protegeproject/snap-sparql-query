package org.semanticweb.owlapi.sparql.parser;

import org.semanticweb.owlapi.sparql.parser.tokenizer.SPARQLParseException;
import org.semanticweb.owlapi.sparql.syntax.SelectExpressionAsVariable;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class ReuseOfVariableInExpressionAs extends ProjectionRestrictionViolation {

    private SelectExpressionAsVariable expr;

    public ReuseOfVariableInExpressionAs(SelectExpressionAsVariable expressionAsVariable) {
        this.expr = expressionAsVariable;
    }

    @Override
    public SPARQLParseException getException() {
        String displayName = expr.getVariable().getDisplayName();
        return SPARQLParseException.getPlainException(
                String.format("Reuse of existing variable (%s) in SELECT AS.", displayName),
                expr.getVariablePosition()

        );
    }
}
