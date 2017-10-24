package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/15
 */
public class Bind {

    private Expression expression;

    private Variable variable;

    public Bind(Expression expression, Variable variable) {
        this.expression = expression;
        this.variable = variable;
    }

    public Expression getExpression() {
        return expression;
    }

    public Variable getVariable() {
        return variable;
    }

    public void evaluate(SolutionMapping solutionMapping, AlgebraEvaluationContext evaluationContext) {
        EvaluationResult eval = expression.evaluate(solutionMapping, evaluationContext);
        if (!eval.isError()) {
            solutionMapping.bind(variable, eval.getResult());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expression, variable);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Bind)) {
            return false;
        }
        Bind other = (Bind) obj;
        return this.expression.equals(other.expression) && this.variable.equals(other.variable);
    }

    @Override
    public String toString() {
        return toStringHelper("Bind")
                .addValue(expression)
                .addValue(variable)
                .toString();
    }
}
