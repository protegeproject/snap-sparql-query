package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 26/06/15
 */
public class HavingCondition {

    private final Expression expression;

    public HavingCondition(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expression);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HavingCondition)) {
            return false;
        }
        HavingCondition other = (HavingCondition) obj;
        return this.expression.equals(other.expression);
    }


    @Override
    public String toString() {
        return toStringHelper("HavingCondition")
                .addValue(expression)
                .toString();
    }
}
