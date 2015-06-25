package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class LiteralVariable extends AbstractVariable implements AtomicLiteral {

    public LiteralVariable(String variableName) {
        super(variableName);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

//    @Override
//    public PrimitiveType getType() {
//        return PrimitiveType.LITERAL;
//    }

    @Override
    public RDFTerm getBound(IRI iri) {
        return Literal.createString(iri.toString());
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
    public Optional<Literal> bind(SolutionMapping sm) {
        Optional<RDFTerm> term = sm.getTermForVariable(this);
        if(term.isPresent() && term.get() instanceof Literal) {
            return Optional.of((Literal) term.get());
        }
        else {
            return Optional.absent();
        }
    }
}
