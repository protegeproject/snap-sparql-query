package org.semanticweb.owlapi.sparql.api;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface Axiom extends Serializable, Visitable, HasCollectVariables {

    <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E;
}
