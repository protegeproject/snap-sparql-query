package org.semanticweb.owlapi.sparql.sparqldl;

import com.google.common.collect.*;
import org.semanticweb.owlapi.sparql.api.RDFTerm;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
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

    public ImmutableList<SolutionMapping> getLeftJoin() {
        int expectedKeys = rightSolutionSequence.size();
        int expectedValuesPerKey = 1;

        Multimap<CompatibilityKey, SolutionMapping> rightMap = ArrayListMultimap.create(expectedKeys, expectedValuesPerKey);

        ImmutableList<Variable> sharedVariables = ImmutableList.copyOf(compatibilityChecker.getVariables());

        rightSolutionSequence.stream()
                .map(sm -> new CompatibilityKey(sharedVariables, sm))
                .forEach(key -> rightMap.put(key, key.getSolutionMapping()));

        ImmutableList.Builder<SolutionMapping> result = ImmutableList.builder();
        for (SolutionMapping left : leftSolutionSequence) {
            CompatibilityKey leftKey = new CompatibilityKey(sharedVariables, left);
            Collection<SolutionMapping> compatibleSolutions = rightMap.get(leftKey);
            for (SolutionMapping right : compatibleSolutions) {
                // NB ImmutableMap.Builder doesn't let us have multiple entries
                HashMap<Variable, RDFTerm> combined = new HashMap<>();
                for(Variable leftVariable : left.getVariables()) {
                    Optional<RDFTerm> leftTerm = left.getTermForVariable(leftVariable);
                    if (leftTerm.isPresent()) {
                        combined.put(leftVariable, leftTerm.get());
                    }
                }
                for(Variable rightVariable : right.getVariables()) {
                    Optional<RDFTerm> rightTerm = right.getTermForVariable(rightVariable);
                    if (rightTerm.isPresent()) {
                        combined.put(rightVariable, rightTerm.get());
                    }
                }
                SolutionMapping join = new SolutionMapping(ImmutableMap.copyOf(combined));
                result.add(join);
            }
            if (compatibleSolutions.isEmpty()) {
                result.add(left);
            }
        }
        return result.build();
    }

    public List<SolutionMapping> getJoin() {
        final List<SolutionMapping> smallerSequence, largerSequence;
        if(rightSolutionSequence.size() < leftSolutionSequence.size()) {
            smallerSequence = rightSolutionSequence;
            largerSequence = leftSolutionSequence;
        }
        else {
            smallerSequence = leftSolutionSequence;
            largerSequence = rightSolutionSequence;
        }

        ImmutableList<Variable> sharedVariables = ImmutableList.copyOf(compatibilityChecker.getVariables());
        Multimap<CompatibilityKey, SolutionMapping> smallerMap = ArrayListMultimap.create(smallerSequence.size(), 1);
        smallerSequence.stream()
                .map(sm -> new CompatibilityKey(sharedVariables, sm))
                .forEach(key -> smallerMap.put(key, key.getSolutionMapping()));
        ImmutableList.Builder<SolutionMapping> result = ImmutableList.builder();
        for (SolutionMapping larger : largerSequence) {
            CompatibilityKey key = new CompatibilityKey(sharedVariables, larger);
            for(SolutionMapping compatibleSolution : smallerMap.get(key)) {
                HashMap<Variable, RDFTerm> combined = new HashMap<>();
                combined.putAll(larger.asMap());
                combined.putAll(compatibleSolution.asMap());
                SolutionMapping join = new SolutionMapping(ImmutableMap.copyOf(combined));
                result.add(join);
            }
        }
        return result.build();
    }
}
