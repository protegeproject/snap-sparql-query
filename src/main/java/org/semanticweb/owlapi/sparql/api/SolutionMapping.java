package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;

import com.google.common.base.Optional;
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

    private Map<Variable, Term> map = new HashMap<Variable, Term>();

    public static SolutionMapping emptyMapping() {
        return emptyMapping;
    }

    private final Timestamp timestamp;

    public SolutionMapping() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public SolutionMapping(Map<Variable, Term> map) {
        for(Variable variable : map.keySet()) {
            this.map.put(checkNotNull(variable), checkNotNull(map.get(variable)));
        }
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }


    public Timestamp getTimestamp() {
        return timestamp;
    }

    public boolean isMapped(Variable variable) {
        return map.get(variable) != null;
    }

    public void bind(Variable variable, Term term) {
        checkNotNull(variable);
        checkNotNull(term);
        map.put(variable, term);
    }

    public Map<Variable, Term> asMap() {
        return new HashMap<Variable, Term>(map);
    }

    public Optional<Term> getTermForVariable(Variable variable) {
        return getTermForVariableName(variable.getName());
    }
    
    public Optional<Term> getTermForVariableName(String variableName) {
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

    public boolean containsAll(SolutionMapping solutionMapping) {
        for(Variable otherVariable : solutionMapping.getVariables()) {
            Optional<Term> t = getTermForVariableName(otherVariable.getName());
            if(!t.isPresent()) {
                return false;
            }
            if(!t.equals(solutionMapping.getTermForVariable(otherVariable))) {
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
