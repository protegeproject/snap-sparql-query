package org.semanticweb.owlapi.sparql.sparqldl;

import com.google.common.base.*;
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
                    com.google.common.base.Optional<RDFTerm> leftTerm = left.getTermForVariable(leftVariable);
                    if (leftTerm.isPresent()) {
                        combined.put(leftVariable, leftTerm.get());
                    }
                }
                for(Variable rightVariable : right.getVariables()) {
                    com.google.common.base.Optional<RDFTerm> rightTerm = right.getTermForVariable(rightVariable);
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
        List<SolutionMapping> result = new ArrayList<>(leftSolutionSequence.size());
        for (SolutionMapping left : leftSolutionSequence) {
            for (SolutionMapping right : rightSolutionSequence) {
                if (compatibilityChecker.isCompatible(left, right)) {
                    ImmutableMap.Builder<Variable, RDFTerm> combined = ImmutableMap.builder();
                    combined.putAll(left.asMap());
                    combined.putAll(right.asMap());
                    SolutionMapping join = new SolutionMapping(combined.build());
                    result.add(join);
                }
            }
        }
        return result;
    }
}
