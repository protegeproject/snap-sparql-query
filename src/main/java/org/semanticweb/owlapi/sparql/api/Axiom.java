package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface Axiom extends Serializable, Visitable, HasCollectVariables, HasBind, HasToOWLObject {

    <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E;

    @Override
    Optional<? extends Axiom> bind(SolutionMapping sm);

    @Override
    OWLAxiom toOWLObject(OWLDataFactory df);
}
