package org.semanticweb.owlapi.sparql.sparqldl;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedHashMultiset;
import org.semanticweb.owlapi.sparql.api.RDFTerm;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

        LinkedHashMultiset<Key> rightKeys = LinkedHashMultiset.create();

        rightSolutionSequence.stream()
                .map(sm -> new Key(sharedVariables, sm))
                .forEach(rightKeys::add);

        ImmutableList.Builder<SolutionMapping> resultBuilder = ImmutableList.builder();

        leftSolutionSequence.stream()
                .map(sm -> new Key(sharedVariables, sm))
                .filter(key -> !rightKeys.contains(key))
                .map(Key::getSolutionMapping)
                .forEach(resultBuilder::add);

        return resultBuilder.build();
    }

    private static class Key {

        private final SolutionMapping solutionMapping;

        private final ImmutableList<Variable> sharedVariables;

        private final int hashCode;

        public Key(ImmutableList<Variable> sharedVariables, SolutionMapping sm) {
            this.solutionMapping = sm;
            this.sharedVariables = sharedVariables;
            int hc = 13;
            for(Variable variable : sharedVariables) {
                hc += sm.getTermForVariable(variable).hashCode();
            }
            hashCode = hc;
        }

        public SolutionMapping getSolutionMapping() {
            return solutionMapping;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Key)) {
                return false;
            }
            Key other = (Key) obj;
            for(Variable variable : sharedVariables) {
                if(!this.solutionMapping.getTermForVariable(variable).equals(other.solutionMapping.getTermForVariable(variable))) {
                    return false;
                }
            }
            return true;
        }
    }
}
