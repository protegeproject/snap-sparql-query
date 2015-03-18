package org.semanticweb.owlapi.sparql.api;


import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface HasIRI extends HasIdentifier {

    IRI getIRI();
}
