package org.semanticweb.owlapi.sparql.sparqldl;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
* Matthew Horridge Stanford Center for Biomedical Informatics Research 01/09/15
*/
class CompatibilityKey {

    private final SolutionMapping solutionMapping;

    private final ImmutableList<Variable> sharedVariables;

    private final int hashCode;

    public CompatibilityKey(ImmutableList<Variable> sharedVariables, SolutionMapping sm) {
        this.solutionMapping = checkNotNull(sm);
        this.sharedVariables = checkNotNull(sharedVariables);
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
        if (!(obj instanceof CompatibilityKey)) {
            return false;
        }
        CompatibilityKey other = (CompatibilityKey) obj;
        for(Variable variable : sharedVariables) {
            if(!this.solutionMapping.getTermForVariable(variable).equals(other.solutionMapping.getTermForVariable(variable))) {
                return false;
            }
        }
        return true;
    }
}
