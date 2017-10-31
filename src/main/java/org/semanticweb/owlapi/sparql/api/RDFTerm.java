package org.semanticweb.owlapi.sparql.api;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 12/06/15
 */
public interface RDFTerm extends HasEvaluation, AnnotationValue, HasCastTo {

    /**
     * Determines if this term is numeric.  To be numeric the term should be a {@link Literal} and
     * Literal{@link #isNumeric()} must be true.
     */
    boolean isNumeric();
}
