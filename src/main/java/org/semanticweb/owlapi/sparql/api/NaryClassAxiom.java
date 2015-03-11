package org.semanticweb.owlapi.sparql.api;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public abstract class NaryClassAxiom implements HasClassExpressions, HasCollectVariables {

    private Set<ClassExpression> classExpressions = new HashSet<ClassExpression>(3);

    protected NaryClassAxiom(Set<ClassExpression> classExpressions) {
        this.classExpressions.addAll(classExpressions);
    }
    
    protected NaryClassAxiom(ClassExpression first, ClassExpression second) {
        this.classExpressions.add(first);
        this.classExpressions.add(second);
    }

    public Set<ClassExpression> getClassExpressions() {
        return Collections.unmodifiableSet(classExpressions);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        for(ClassExpression ce : classExpressions) {
            ce.collectVariables(variables);
        }
    }
}
