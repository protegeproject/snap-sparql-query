package org.semanticweb.owlapi.sparql.syntax;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.sparql.api.UntypedVariable;
import org.semanticweb.owlapi.sparql.parser.tokenizer.TokenPosition;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class SelectVariable extends SelectItem {

    private final UntypedVariable variable;

    public SelectVariable(UntypedVariable variable, TokenPosition tokenPosition) {
        super(tokenPosition, tokenPosition);
        this.variable = checkNotNull(variable);
    }

    public UntypedVariable getVariable() {
        return variable;
    }

    @Override
    public boolean isAggregate() {
        return false;
    }

    @Override
    public boolean isVariable() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode("SelectVariable", variable);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SelectVariable)) {
            return false;
        }
        SelectVariable other = (SelectVariable) obj;
        return this.variable.equals(other.variable);
    }


    @Override
    public String toString() {
        return toStringHelper("SelectVariable")
                .addValue(variable)
                .toString();
    }


}
