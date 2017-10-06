package org.semanticweb.owlapi.sparql.parser.tokenizer;

import java.util.Optional;
import org.semanticweb.owlapi.sparql.api.PrimitiveType;
import org.semanticweb.owlapi.sparql.api.UntypedVariable;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 12/06/15
 */
public class VariableTypeManager {

    private Map<UntypedVariable, PrimitiveType> typeMap = new HashMap<>();

    public void setVariableType(UntypedVariable variable, PrimitiveType variableType) {
        typeMap.put(checkNotNull(variable), checkNotNull(variableType));
    }

    public Optional<PrimitiveType> getVariableType(UntypedVariable variable) {
        return Optional.ofNullable(typeMap.get(variable));
    }
}
