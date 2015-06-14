package org.semanticweb.owlapi.sparql.sparqldl;

import com.google.common.collect.ImmutableMap;
import de.derivo.sparqldlapi.QueryArgument;
import de.derivo.sparqldlapi.QueryBinding;
import de.derivo.sparqldlapi.impl.LiteralTranslator;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.sparql.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 21/03/15
 */
public class SolutionMappingTranslator {

    public SolutionMappingTranslator() {
    }

    /**
     * Translate the specified query binding into a solution mapping
     * @param binding The binding to translate. Not {@code null}.
     * @param nameVariableMap A map from variable names, to variables.  Not {@code null}.
     * @return The tranlated solution mapping.
     */
    public SolutionMapping translate(QueryBinding binding, Map<String, Variable> nameVariableMap) {
        Set<QueryArgument> boundArgs = binding.getBoundArgs();
        ImmutableMap.Builder<Variable, RDFTerm> solutionMapping = ImmutableMap.builder();
        for(QueryArgument arg : boundArgs) {
            Variable var = nameVariableMap.get(arg.getValueAsVar().getName());
            QueryArgument value = binding.get(arg);
            RDFTerm term = null;
            switch (value.getType()) {
                case VAR:
                    // Shouldn't happen
                    break;
                case URI:
                    IRI iri = value.getValueAsIRI();
                    term = var.getBound(iri);
                    break;
                case BNODE:
                    // TODO: Log
                    break;
                case LITERAL:
                    OWLLiteral literal = value.getValueAsLiteral();
                    term = new Literal(
                            Datatype.get(literal.getDatatype().getIRI()),
                            literal.getLiteral(),
                            literal.getLang());
                    break;
            }
            if(term != null) {
                solutionMapping.put(var, term);
            }
        }
        return new SolutionMapping(solutionMapping.build());
    }
}
