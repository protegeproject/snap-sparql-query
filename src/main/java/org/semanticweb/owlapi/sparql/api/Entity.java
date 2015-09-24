package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface Entity extends HasIRI, HasPrefixedName, HasCollectVariables, HasAnnotationSubject, HasAsRDFTerm, HasToOWLObject {
    @Override
    OWLEntity toOWLObject(OWLDataFactory df);
}
