package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface AnnotationValue extends Visitable, HasCollectVariables, HasBind, HasToOWLObject {
    @Override
    Optional<? extends AnnotationValue> bind(SolutionMapping sm);

    @Override
    OWLAnnotationValue toOWLObject(OWLDataFactory df);
}
