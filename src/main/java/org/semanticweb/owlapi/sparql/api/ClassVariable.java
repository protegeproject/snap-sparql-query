package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class ClassVariable extends AbstractVariable implements AtomicClass {

    public ClassVariable(String variableName) {
        super(variableName);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public RDFTerm getBound(IRI iri) {
        return new AtomicIRI(iri);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        variables.add(this);
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }

    @Override
    public String toString() {
        return toStringHelper("ClassVariable")
                .addValue(getVariableNamePrefix().getPrefix())
                .addValue(getName())
                .toString();
    }

    @Override
    public Optional<NamedClass> bind(SolutionMapping sm) {
        Optional<AtomicIRI> term = sm.getIRIForVariable(this);
        if(term.isPresent()) {
            return Optional.of(new NamedClass(term.get().getIRI()));
        }
        else {
            return Optional.absent();
        }
    }

    @Override
    public OWLClass toOWLObject(OWLDataFactory df) {
        throw new UnboundVariableTranslationException();
    }
}
