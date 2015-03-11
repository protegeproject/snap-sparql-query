package org.semanticweb.owlapi.sparql.api;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public abstract class ObjectPropertyCharacteristic implements Axiom, HasProperty<ObjectPropertyExpression> {

    private ObjectPropertyExpression property;

    protected ObjectPropertyCharacteristic(ObjectPropertyExpression property) {
        this.property = property;
    }

    public ObjectPropertyExpression getProperty() {
        return property;
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        property.collectVariables(variables);
    }
}
