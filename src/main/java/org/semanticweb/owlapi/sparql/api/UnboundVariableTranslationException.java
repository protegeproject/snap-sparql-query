package org.semanticweb.owlapi.sparql.api;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 23/09/15
 */
public class UnboundVariableTranslationException extends RuntimeException {

    public UnboundVariableTranslationException() {
        super("Cannot translate unbound variable to OWLObject");
    }
}
