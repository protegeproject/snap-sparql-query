package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class SubObjectPropertyOf implements Axiom, HasSubProperty<ObjectPropertyExpression>, HasSuperProperty<ObjectPropertyExpression>, HasObjectPropertyExpressions {

    private ObjectPropertyExpression subProperty;

    private ObjectPropertyExpression superProperty;

    public SubObjectPropertyOf(ObjectPropertyExpression subProperty, ObjectPropertyExpression superProperty) {
        this.subProperty = subProperty;
        this.superProperty = superProperty;
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public ObjectPropertyExpression getSubProperty() {
        return subProperty;
    }

    public ObjectPropertyExpression getSuperProperty() {
        return superProperty;
    }

    public Set<ObjectPropertyExpression> getObjectPropertyExpressions() {
        return new HashSet<ObjectPropertyExpression>(Arrays.asList(subProperty, superProperty));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(subProperty.hashCode(), superProperty.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SubObjectPropertyOf)) {
            return false;
        }
        SubObjectPropertyOf other = (SubObjectPropertyOf) obj;
        return this.subProperty.equals(other.subProperty) && this.superProperty.equals(other.superProperty);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        subProperty.collectVariables(variables);
        superProperty.collectVariables(variables);
    }

    @Override
    public Optional<SubObjectPropertyOf> bind(SolutionMapping sm) {
        Optional<? extends ObjectPropertyExpression> subProperty = this.subProperty.bind(sm);
        if(!subProperty.isPresent()) {
            return Optional.absent();
        }
        Optional<? extends ObjectPropertyExpression> superProperty = this.superProperty.bind(sm);
        if(!superProperty.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(new SubObjectPropertyOf(subProperty.get(), superProperty.get()));
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLSubObjectPropertyOfAxiom(
                getSubProperty().toOWLObject(df),
                getSuperProperty().toOWLObject(df)
        );
    }
}
