package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface Atomic extends Term, Expression, Serializable, HasCollectVariables, HasBind {
    @Override
    Optional<? extends Atomic> bind(SolutionMapping sm);
}
