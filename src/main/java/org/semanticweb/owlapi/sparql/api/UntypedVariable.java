package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;

import javax.swing.text.html.Option;
import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/07/2012
 */
public class UntypedVariable extends AbstractVariable  {

    public UntypedVariable(String variableName) {
        super(variableName);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public RDFTerm getBound(IRI iri) {
        return new AtomicIRI(iri);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        variables.add(this);
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("UntypedVariable")
                .addValue(getDisplayName())
                .toString();
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }

    @Override
    public Optional<Atomic> bind(SolutionMapping sm) {
        Optional<RDFTerm> term = sm.getTermForVariable(this);
        if(!term.isPresent()) {
            return Optional.absent();
        }
        RDFTerm t = term.get();
        if(t instanceof Literal) {
            return Optional.of((Literal) t);
        }
        else if(t instanceof AtomicIRI) {
            return Optional.of((AtomicIRI) t);
        }
        else if(t instanceof AnonymousIndividual) {
            return Optional.of((AnonymousIndividual) t);
        }
        return Optional.absent();
    }

    public OWLObject toOWLObject(OWLDataFactory df) {
        throw new UnboundVariableTranslationException();
    }

    @Override
    public boolean isEntityVariable() {
        return false;
    }
}
