package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface Atomic extends Term, Expression, Serializable, HasCollectVariables, HasBind, HasToOWLObject {

    @Override
    OWLObject toOWLObject(OWLDataFactory df);

    @Nonnull
    java.util.Optional<Variable> asVariable();
}
