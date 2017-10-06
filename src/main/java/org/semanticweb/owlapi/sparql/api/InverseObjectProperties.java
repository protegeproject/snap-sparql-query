package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/08/2012
 */
public class InverseObjectProperties extends NaryObjectPropertyAxiom implements Axiom {

    private final ObjectPropertyExpression left;

    private final ObjectPropertyExpression right;

    public InverseObjectProperties(ObjectPropertyExpression left, ObjectPropertyExpression right) {
        super(left, right);
        this.left = left;
        this.right = right;
    }

    public ObjectPropertyExpression getLeft() {
        return left;
    }

    public ObjectPropertyExpression getRight() {
        return right;
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return getObjectPropertyExpressions().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof InverseObjectProperties)) {
            return false;
        }
        InverseObjectProperties other = (InverseObjectProperties) obj;
        return this.getObjectPropertyExpressions().equals(other.getObjectPropertyExpressions());
    }

    @Override
    public Optional<InverseObjectProperties> bind(SolutionMapping sm) {
        Optional<? extends ObjectPropertyExpression> boundLeft = getLeft().bind(sm);
        if(!boundLeft.isPresent()) {
            return Optional.empty();
        }
        Optional<? extends ObjectPropertyExpression> boundRight = getRight().bind(sm);
        if(!boundRight.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new InverseObjectProperties(boundLeft.get(), boundRight.get()));
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLInverseObjectPropertiesAxiom(
                left.toOWLObject(df),
                right.toOWLObject(df)
        );
    }
}
