package org.semanticweb.owlapi.sparql.builtin;

import org.semanticweb.owlapi.sparql.api.Literal;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 10/06/15
 */
public class LiteralCompatibilityChecker {

    /**
     * The SPARQL spec says this:
     *
     * Compatibility of two arguments is defined as:
     *
     *      - The arguments are simple literals or literals typed as xsd:string
     *      - The arguments are plain literals with identical language tags
     *      - The first argument is a plain literal with language tag and the second argument is a simple
     *          literal or literal typed as xsd:string
     *
     *  The first point is ambiguous, but it means any combination of simple or string literals.
     *
     * @param left The left literal.
     * @param right The right literal
     * @return true of the literals are argument compatible, otherwise false.
     */
    public boolean isCompatibleWith(Literal left, Literal right) {
        if((left.isSimpleLiteral() || left.isStringLiteral()) && (right.isSimpleLiteral() || right.isStringLiteral())) {
            return true;
        }
        if(left.isRDFPlainLiteral() && right.isRDFPlainLiteral() && left.getLang().equals(right.getLang())) {
            return true;
        }
        if(left.isRDFPlainLiteral() && (right.isSimpleLiteral() || right.isStringLiteral())) {
            return true;
        }
        return false;
    }
}
