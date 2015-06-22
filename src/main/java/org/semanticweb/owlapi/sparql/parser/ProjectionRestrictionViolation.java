package org.semanticweb.owlapi.sparql.parser;

import org.semanticweb.owlapi.sparql.parser.tokenizer.SPARQLParseException;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 *
 * Describes a violation in the projection clause restrictions.
 */
public abstract class ProjectionRestrictionViolation {

    /**
     * Get the violation as a parse exception.
     * @return The parse exception.
     */
    public abstract SPARQLParseException getException();
}
