package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import jpaul.Constraints.Var;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;

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
    public PrimitiveType getType() {
        return PrimitiveType.CLASS;
    }

    @Override
    public RDFTerm getBound(IRI iri) {
        return new AtomicIRI(iri);
    }

//    @Override
//    public int hashCode() {
//        return ClassVariable.class.getSimpleName().hashCode() + getName().hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if(obj == this) {
//            return true;
//        }
//        if(!(obj instanceof Variable)) {
//            return false;
//        }
//        Variable other = (Variable) obj;
//        return other.getName().equals(this.getName());
//    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        variables.add(this);
    }


    @Override
    public String toString() {
        return toStringHelper("ClassVariable")
                .addValue(getVariableNamePrefix().getPrefix())
                .addValue(getName())
                .toString();
    }
}
