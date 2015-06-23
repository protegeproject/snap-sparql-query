package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class DataPropertyVariable extends AbstractVariable implements AtomicDataProperty{

    public DataPropertyVariable(String variableName) {
        super(variableName);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.DATA_PROPERTY;
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
}
