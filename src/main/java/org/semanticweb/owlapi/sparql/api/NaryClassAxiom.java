package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    protected Optional<Set<ClassExpression>> getBoundClassExpressions(SolutionMapping sm) {
        Set<ClassExpression> result = new HashSet<>();
        for(ClassExpression ce : result) {
            Optional<? extends ClassExpression> boundCe = ce.bind(sm);
            if(!boundCe.isPresent()) {
                return Optional.absent();
            }
            else {
                result.add(boundCe.get());
            }
        }
        return Optional.of(result);
    }
}
