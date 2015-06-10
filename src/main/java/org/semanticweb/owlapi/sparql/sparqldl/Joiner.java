package org.semanticweb.owlapi.sparql.sparqldl;

import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Term;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.*;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 07/06/15
 */
public class Joiner {

    private List<SolutionMapping> leftSolutionSequence;
    private List<SolutionMapping> rightSolutionSequence;
    private CompatibilityChecker compatibilityChecker;

    public Joiner(List<SolutionMapping> leftSolutionSequence, List<SolutionMapping> rightSolutionSequence, Set<Variable> joinVariables) {
        this.leftSolutionSequence = leftSolutionSequence;
        this.rightSolutionSequence = rightSolutionSequence;
        compatibilityChecker = new CompatibilityChecker(joinVariables);
    }

    public List<SolutionMapping> getLeftJoin() {
        List<SolutionMapping> result = new ArrayList<>(leftSolutionSequence.size());
        for(SolutionMapping left : leftSolutionSequence) {
            boolean joined = false;
            for(SolutionMapping right : rightSolutionSequence) {
                if(compatibilityChecker.isCompatible(left, right)) {
                    joined = true;
                    Map<Variable, Term> map = left.asMap();
                    map.putAll(right.asMap());
                    SolutionMapping join = new SolutionMapping(map);
                    result.add(join);
                }
            }
            if(!joined) {
                result.add(left);
            }

        }
        return result;
    }

    public List<SolutionMapping> getJoin() {
        List<SolutionMapping> result = new ArrayList<>(leftSolutionSequence.size());
        for(SolutionMapping left : leftSolutionSequence) {
            for(SolutionMapping right : rightSolutionSequence) {
                if(compatibilityChecker.isCompatible(left, right)) {
                    Map<Variable, Term> map = left.asMap();
                    map.putAll(right.asMap());
                    SolutionMapping join = new SolutionMapping(map);
                    result.add(join);
                }
            }
        }
        return result;
    }
}
