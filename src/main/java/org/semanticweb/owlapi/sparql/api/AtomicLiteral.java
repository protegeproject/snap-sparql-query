package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface AtomicLiteral extends Atomic, AnnotationValue {

    @Override
    Optional<? extends AtomicLiteral> bind(SolutionMapping sm);

    @Override
    OWLLiteral toOWLObject(OWLDataFactory df);
}
