package org.semanticweb.owlapi.sparql.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/08/2012
 */
public interface Visitable {

    <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E;
}
