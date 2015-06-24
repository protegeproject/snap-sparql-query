package org.semanticweb.owlapi.sparql.sparqldl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import de.derivo.sparqldlapi.QueryBinding;
import de.derivo.sparqldlapi.QueryResult;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.HashMap;
import java.util.Map;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 09/06/15
 */
public class ResultTranslator {

    private SolutionMappingTranslator solutionMappingTranslator;

    private ImmutableSet<Variable> variables;

    public ResultTranslator(SolutionMappingTranslator solutionMappingTranslator,
                            ImmutableSet<Variable> variables) {
        this.solutionMappingTranslator = solutionMappingTranslator;
        this.variables = variables;
    }

    public ImmutableList<SolutionMapping> translateResult(QueryResult result) {
        Map<String, Variable> nameVariableMap = new HashMap<>();
        for(Variable variable : variables) {
            nameVariableMap.put(variable.getName(), variable);
        }
        ImmutableList.Builder<SolutionMapping> solutionSequence = ImmutableList.builder();
        for(int i = 0; i < result.size(); i++) {
            QueryBinding binding = result.get(i);
            SolutionMapping sm = solutionMappingTranslator.translate(binding, nameVariableMap);
            solutionSequence.add(sm);
        }
        return solutionSequence.build();
    }
}
