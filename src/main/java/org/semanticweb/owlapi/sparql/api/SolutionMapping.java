package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.semanticweb.owlapi.sparql.builtin.Timestamp;

import java.util.*;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/07/2012
 */
public class SolutionMapping {

    private static SolutionMapping emptyMapping = new SolutionMapping();

    private final ImmutableMap<Variable, RDFTerm> map;

    public static SolutionMapping emptyMapping() {
        return emptyMapping;
    }

    public SolutionMapping() {
        map = ImmutableMap.of();
    }

    public SolutionMapping(ImmutableMap<Variable, RDFTerm> map) {
        this.map = map;
    }

    public boolean isMapped(Variable variable) {
        return map.get(variable) != null;
    }

    public void bind(Variable variable, RDFTerm term) {
        checkNotNull(variable);
        checkNotNull(term);
        map.put(variable, term);
    }

    public ImmutableMap<Variable, RDFTerm> asMap() {
        return map;
    }

    public Optional<RDFTerm> getTermForVariable(Variable variable) {
        return Optional.fromNullable(map.get(variable));
    }
    
    public Optional<RDFTerm> getTermForVariableName(String variableName) {
        String searchString = variableName;
        if(variableName.startsWith("?") || variableName.startsWith("$")) {
            searchString = variableName.substring(1);
        }
        for(Variable var : map.keySet()) {
            if(var.getName().equals(searchString)) {
                return Optional.fromNullable(map.get(var));
            }
        }
        return Optional.absent();
    }

    public Collection<Variable> getVariables() {
        return new ArrayList<>(map.keySet());
    }

    public SolutionMapping projectForVariables(Collection<Variable> variables) {
        if(map.keySet().equals(variables)) {
            return this;
        }
        else {
            ImmutableMap.Builder<Variable, RDFTerm> projectedMapping = ImmutableMap.builder();
            for(Variable variable : variables) {
                RDFTerm value = map.get(variable);
                if (value != null) {
                    projectedMapping.put(variable, value);
                }
            }
            return new SolutionMapping(projectedMapping.build());
        }
    }

    public boolean containsAll(SolutionMapping solutionMapping) {
        for(Variable otherVariable : solutionMapping.getVariables()) {
            if(!map.containsKey(otherVariable)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(map);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SolutionMapping)) {
            return false;
        }
        SolutionMapping other = (SolutionMapping) obj;
        return this.map.equals(other.map);
    }


    @Override
    public String toString() {
        return toStringHelper("SolutionMapping")
                .addValue(map)
                .toString();
    }
}
