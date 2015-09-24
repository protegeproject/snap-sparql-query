package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;

import java.util.Collection;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 24/06/15
 */
public class IRIVariable extends AbstractVariable implements AnnotationSubject, AnnotationValue, Atomic {

    public IRIVariable(String variableName) {
        super(variableName);
    }

    @Override
    public RDFTerm getBound(IRI iri) {
        return new AtomicIRI(iri);
    }

    @Override
    public Optional<AtomicIRI> bind(SolutionMapping sm) {
        return sm.getIRIForVariable(this);
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        variables.add(this);
    }

    @Override
    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public IRI toOWLObject(OWLDataFactory df) {
        throw new UnboundVariableTranslationException();
    }
}
