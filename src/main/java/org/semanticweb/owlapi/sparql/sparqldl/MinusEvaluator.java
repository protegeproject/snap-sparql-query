package org.semanticweb.owlapi.sparql.sparqldl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedHashMultiset;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 07/06/15
 */
public class MinusEvaluator {

    private final ImmutableList<SolutionMapping> leftSolutionSequence;

    private final ImmutableList<SolutionMapping> rightSolutionSequence;

    private final ImmutableList<Variable> sharedVariables;

    public MinusEvaluator(ImmutableList<SolutionMapping> leftSolutionSequence, ImmutableList<SolutionMapping> rightSolutionSequence, Set<Variable> sharedVariables) {
        this.leftSolutionSequence = leftSolutionSequence;
        this.rightSolutionSequence = rightSolutionSequence;
        this.sharedVariables = ImmutableList.copyOf(sharedVariables);
    }

    public ImmutableList<SolutionMapping> getMinus() {
        if (sharedVariables.isEmpty()) {
            return leftSolutionSequence;
        }

        LinkedHashMultiset<CompatibilityKey> rightKeys = LinkedHashMultiset.create();

        rightSolutionSequence.stream()
                .map(sm -> new CompatibilityKey(sharedVariables, sm))
                .forEach(rightKeys::add);

        ImmutableList.Builder<SolutionMapping> resultBuilder = ImmutableList.builder();

        leftSolutionSequence.stream()
                .map(sm -> new CompatibilityKey(sharedVariables, sm))
                .filter(key -> !rightKeys.contains(key))
                .map(CompatibilityKey::getSolutionMapping)
                .forEach(resultBuilder::add);

        return resultBuilder.build();
    }

}
