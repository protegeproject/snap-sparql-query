package org.semanticweb.owlapi.sparql.parser.tokenizer;

import com.google.common.base.Objects;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 19/06/15
 */
public class CommentTokenType extends TokenType {

    private static final CommentTokenType instance = new CommentTokenType();

    public static CommentTokenType get() {
        return instance;
    }

    @Override
    public <R, E extends Throwable> R accept(TokenTypeVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "CommentTokenType".hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof CommentTokenType;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("CommentTokenType")
                .toString();
    }
}
