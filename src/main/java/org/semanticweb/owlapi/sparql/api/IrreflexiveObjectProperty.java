package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class IrreflexiveObjectProperty extends ObjectPropertyCharacteristic {

    public IrreflexiveObjectProperty(ObjectPropertyExpression property) {
        super(property);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return IrreflexiveObjectProperty.class.getSimpleName().hashCode() + getProperty().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof IrreflexiveObjectProperty)) {
            return false;
        }
        IrreflexiveObjectProperty other = (IrreflexiveObjectProperty) obj;
        return this.getProperty().equals(other.getProperty());
    }

    @Override
    public Optional<IrreflexiveObjectProperty> bind(SolutionMapping sm) {
        Optional<? extends ObjectPropertyExpression> property = getProperty().bind(sm);
        if(!property.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(new IrreflexiveObjectProperty(property.get()));
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLIrreflexiveObjectPropertyAxiom(getProperty().toOWLObject(df));
    }
}
