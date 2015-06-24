package org.semanticweb.owlapi.sparql.syntax;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.sparql.api.BuiltInCallExpression;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.UntypedVariable;
import org.semanticweb.owlapi.sparql.parser.tokenizer.TokenPosition;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/07/2012
 */
public class SelectExpressionAsVariable extends SelectItem {

    private final Expression expression;

    private final TokenPosition variablePosition;

    private final UntypedVariable variable;



    public SelectExpressionAsVariable(Expression expression, UntypedVariable variable, TokenPosition start, TokenPosition end, TokenPosition variablePosition) {
        super(start, end);
        this.variablePosition = checkNotNull(variablePosition);
        this.expression = checkNotNull(expression);
        this.variable = checkNotNull(variable);
    }

    public SelectExpressionAsVariable(Expression expression, UntypedVariable variable) {
        this(expression, variable, TokenPosition.empty(), TokenPosition.empty(), TokenPosition.empty());
    }

    @Override
    public boolean isVariable() {
        return false;
    }

    public Expression getExpression() {
        return expression;
    }

    public UntypedVariable getVariable() {
        return variable;
    }

    public TokenPosition getVariablePosition() {
        return variablePosition;
    }

    public boolean isAggregate() {
        return expression instanceof BuiltInCallExpression && ((BuiltInCallExpression) expression).isAggregate();
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
        if (!(obj instanceof SelectExpressionAsVariable)) {
            return false;
        }
        SelectExpressionAsVariable other = (SelectExpressionAsVariable) obj;
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
