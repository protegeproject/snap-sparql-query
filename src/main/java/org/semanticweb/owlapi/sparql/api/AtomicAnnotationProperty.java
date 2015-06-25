package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface AtomicAnnotationProperty extends AtomicProperty {

    @Override
    Optional<? extends AtomicAnnotationProperty> bind(SolutionMapping sm);
}
