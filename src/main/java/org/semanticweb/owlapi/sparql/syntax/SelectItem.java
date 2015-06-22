package org.semanticweb.owlapi.sparql.syntax;

import org.semanticweb.owlapi.sparql.api.UntypedVariable;
import org.semanticweb.owlapi.sparql.parser.tokenizer.TokenPosition;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public abstract class SelectItem {

    private final TokenPosition start;

    private final TokenPosition end;

    public SelectItem(TokenPosition start, TokenPosition end) {
        this.start = start;
        this.end = end;
    }

    public abstract UntypedVariable getVariable();

    public abstract boolean isAggregate();

    public abstract boolean isVariable();



    public TokenPosition getStartTokenPosition() {
        return start;
    }

    public TokenPosition getEndTokenPosition() {
        return end;
    }
}
