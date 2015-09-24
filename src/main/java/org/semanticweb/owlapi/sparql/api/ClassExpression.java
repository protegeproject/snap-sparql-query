package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface ClassExpression extends Visitable, HasCollectVariables, HasBind, HasToOWLObject {
    @Override
    Optional<? extends ClassExpression> bind(SolutionMapping sm);

    @Override
    OWLClassExpression toOWLObject(OWLDataFactory df);
}
