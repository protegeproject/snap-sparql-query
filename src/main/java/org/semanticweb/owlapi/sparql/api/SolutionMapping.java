package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

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

    private final Map<Variable, RDFTerm> map;

    public static SolutionMapping emptyMapping() {
        return emptyMapping;
    }

    public SolutionMapping() {
        map = new HashMap<>(3);
    }

    public SolutionMapping(ImmutableMap<Variable, RDFTerm> map) {
        this.map = new HashMap<>(checkNotNull(map));
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
        return ImmutableMap.copyOf(map);
    }

    public Optional<RDFTerm> getTermForVariable(Variable variable) {
        return Optional.ofNullable(map.get(variable));
    }

    public java.util.Optional<AtomicIRI> getIRIForVariable(Variable variable) {
        RDFTerm term = map.get(variable);
        if(term instanceof AtomicIRI) {
            return Optional.of((AtomicIRI) term);
        }
        else {
            return Optional.empty();
        }
    }
    
    public Optional<RDFTerm> getTermForVariableName(String variableName) {
        String searchString = variableName;
        if(variableName.startsWith("?") || variableName.startsWith("$")) {
            searchString = variableName.substring(1);
        }
        for(Variable var : map.keySet()) {
            if(var.getName().equals(searchString)) {
                return Optional.ofNullable(map.get(var));
            }
        }
        return Optional.empty();
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
