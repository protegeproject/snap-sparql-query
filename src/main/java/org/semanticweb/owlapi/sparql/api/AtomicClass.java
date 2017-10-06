package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface AtomicClass extends Atomic, ClassExpression {
    @Override
    Optional<? extends AtomicClass> bind(SolutionMapping sm);
}
