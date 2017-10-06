package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface ObjectPropertyExpression extends Visitable, HasCollectVariables, HasBind, HasToOWLObject {
    @Override
    Optional<? extends ObjectPropertyExpression> bind(SolutionMapping sm);

    @Override
    OWLObjectPropertyExpression toOWLObject(OWLDataFactory df);
}
