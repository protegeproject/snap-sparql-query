package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 24/06/15
 */
public interface HasBind {

    Optional<?> bind(SolutionMapping sm);
}
