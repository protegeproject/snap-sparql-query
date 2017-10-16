package org.semanticweb.owlapi.sparql.parser.tokenizer;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Oct 2017
 */
public class ScalarKeyTokenType extends TokenType {

    private static final ScalarKeyTokenType instance = new ScalarKeyTokenType();

    private ScalarKeyTokenType() {
    }

    public static ScalarKeyTokenType get() {
        return instance;
    }

    @Override
    public <R, E extends Throwable> R accept(TokenTypeVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}

