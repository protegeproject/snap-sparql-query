package org.semanticweb.owlapi.sparql.api;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class EquivalentClasses extends NaryClassAxiom implements ClassAxiom {

    public EquivalentClasses(Set<ClassExpression> classExpressions) {
        super(classExpressions);
    }

    public EquivalentClasses(ClassExpression first, ClassExpression second) {
        super(first, second);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return EquivalentClasses.class.getSimpleName().hashCode() + getClassExpressions().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EquivalentClasses)) {
            return false;
        }
        EquivalentClasses other = (EquivalentClasses) obj;
        return this.getClassExpressions().equals(other.getClassExpressions());
    }
}
