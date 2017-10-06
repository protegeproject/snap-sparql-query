package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface AtomicAnnotationProperty extends AtomicProperty {

    @Override
    Optional<? extends AtomicAnnotationProperty> bind(SolutionMapping sm);

    @Override
    OWLAnnotationProperty toOWLObject(OWLDataFactory df);
}
