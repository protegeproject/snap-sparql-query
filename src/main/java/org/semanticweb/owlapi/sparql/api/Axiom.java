package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface Axiom extends Serializable, Visitable, HasCollectVariables, HasBind {

    <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E;

    @Override
    Optional<? extends Axiom> bind(SolutionMapping sm);
}
