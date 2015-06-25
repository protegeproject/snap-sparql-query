package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collection;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 25/06/15
 */
public class AnnotationValueVariable extends AbstractVariable implements AnnotationValue {

    public AnnotationValueVariable(String variableName) {
        super(variableName);
    }

    @Override
    public RDFTerm getBound(IRI iri) {
        return new AtomicIRI(iri);
    }

    @Override
    public Optional<? extends AnnotationValue> bind(SolutionMapping sm) {
        Optional<RDFTerm> term = sm.getTermForVariable(this);
        if(!term.isPresent()) {
            return Optional.absent();
        }
        if(term.get() instanceof Literal) {
            return Optional.of((Literal) term.get());
        }
        else if(term.get() instanceof AtomicIRI) {
            return Optional.of((AtomicIRI) term.get());
        }
        else if(term.get() instanceof AnonymousIndividual) {
            return Optional.of((AnonymousIndividual) term.get());
        }
        return Optional.absent();
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
}
