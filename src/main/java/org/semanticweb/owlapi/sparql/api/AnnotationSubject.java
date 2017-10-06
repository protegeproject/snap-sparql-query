package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface AnnotationSubject extends Visitable, HasCollectVariables, HasBind, HasToOWLObject {
    @Override
    Optional<? extends AnnotationSubject> bind(SolutionMapping sm);

    @Override
    OWLAnnotationSubject toOWLObject(OWLDataFactory df);
}
