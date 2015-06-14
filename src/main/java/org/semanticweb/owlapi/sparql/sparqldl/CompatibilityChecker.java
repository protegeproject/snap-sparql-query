package org.semanticweb.owlapi.sparql.sparqldl;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.sparql.api.RDFTerm;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Term;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 07/06/15
 */
public class CompatibilityChecker {

    Set<Variable> variables;

    public CompatibilityChecker(Set<Variable> variables) {
        this.variables = variables;
    }

    public Set<Variable> getVariables() {
        return variables;
    }

    public boolean isEmpty() {
        return variables.isEmpty();
    }


    public boolean isCompatible(SolutionMapping left, SolutionMapping right) {
        for(Variable var : variables) {
            Optional<RDFTerm> leftTerm = left.getTermForVariable(var);
            Optional<RDFTerm> rightTerm = right.getTermForVariable(var);

            if(rightTerm.isPresent()) {
                if(leftTerm.isPresent()) {
                    if(!leftTerm.get().equals(rightTerm.get())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
