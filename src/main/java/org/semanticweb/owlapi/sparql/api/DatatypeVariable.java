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
public class DatatypeVariable extends AbstractVariable implements AtomicDatatype {

    public DatatypeVariable(String variableName) {
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

//    @Override
//    public int hashCode() {
//        return DatatypeVariable.class.getSimpleName().hashCode() + getName().hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if(obj == this) {
//            return true;
//        }
//        if(!(obj instanceof DatatypeVariable)) {
//            return false;
//        }
//        DatatypeVariable other = (DatatypeVariable) obj;
//        return other.getName().equals(this.getName());
//    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        variables.add(this);
    }
}
