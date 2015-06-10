package org.semanticweb.owlapi.sparql.syntax;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.UntypedVariable;
import org.semanticweb.owlapi.sparql.api.Variable;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/07/2012
 */
public class SelectAs extends SelectItem {

    private Expression expression;

    private UntypedVariable variable;

    public SelectAs(Expression expression, UntypedVariable variable) {
        this.expression = expression;
        this.variable = variable;
    }

    public Expression getExpression() {
        return expression;
    }

    public UntypedVariable getVariable() {
        return variable;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode("SelectAs", expression, variable);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SelectAs)) {
            return false;
        }
        SelectAs other = (SelectAs) obj;
        return this.expression.equals(other.expression) && this.variable.equals(other.variable);
    }


    @Override
    public String toString() {
        return toStringHelper("SelectAs")
                .addValue(expression)
                .addValue(variable)
                .toString();
    }
}
