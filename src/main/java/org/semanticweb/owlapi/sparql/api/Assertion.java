package org.semanticweb.owlapi.sparql.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface Assertion<S extends Visitable, P extends Visitable, O extends Visitable> extends Axiom, HasSubject<S>, HasProperty<P>, HasObject<O> {

}
