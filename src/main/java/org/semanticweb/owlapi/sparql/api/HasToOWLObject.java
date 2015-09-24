package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 23/09/15
 */
public interface HasToOWLObject {

    OWLObject toOWLObject(OWLDataFactory df);
}
