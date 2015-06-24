package org.semanticweb.owlapi.sparql.sparqldl;

import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 07/06/15
 */
public class Minus {

    private List<SolutionMapping> leftSolutionSequence;
    private List<SolutionMapping> rightSolutionSequence;
    private CompatibilityChecker compatibilityChecker;

    public Minus(List<SolutionMapping> leftSolutionSequence, List<SolutionMapping> rightSolutionSequence, Set<Variable> joinVariables) {
        this.leftSolutionSequence = leftSolutionSequence;
        this.rightSolutionSequence = rightSolutionSequence;
        this.compatibilityChecker = new CompatibilityChecker(joinVariables);
    }

    public List<SolutionMapping> getMinus() {
        if(compatibilityChecker.isEmpty()) {
            return leftSolutionSequence;
        }
        List<SolutionMapping> result = new ArrayList<>(leftSolutionSequence.size());
        for(SolutionMapping left : leftSolutionSequence) {
            boolean remove = false;
            for(SolutionMapping right : rightSolutionSequence) {
                if(compatibilityChecker.isCompatible(left, right)) {
                    remove = true;
                    break;
                }
            }
            if(!remove) {
                result.add(left);
            }
        }
        return result;
    }
}
