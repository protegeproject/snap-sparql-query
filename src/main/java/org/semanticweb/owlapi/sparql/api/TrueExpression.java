package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;

import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 09/06/15
 */
public class TrueExpression implements Expression {

    @Override
    public EvaluationResult evaluate(SolutionMapping sm) {
        return EvaluationResult.getTrue();
    }

    @Override
    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm) {
        return EvaluationResult.getTrue();
    }

    @Override
    public EvaluationResult evaluateAsLiteral(SolutionMapping sm) {
        return EvaluationResult.getTrue();
    }

    @Override
    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm) {
        return EvaluationResult.getTrue();
    }

    @Override
    public EvaluationResult evaluateAsStringLiteral(SolutionMapping sm) {
        return EvaluationResult.getTrue();
    }

    @Override
    public EvaluationResult evaluateAsNumeric(SolutionMapping sm) {
        return EvaluationResult.getTrue();
    }

    @Override
    public EvaluationResult evaluateAsDateTime(SolutionMapping sm) {
        return EvaluationResult.getTrue();
    }

    @Override
    public EvaluationResult evaluateAsIRI(SolutionMapping sm) {
        return EvaluationResult.getTrue();
    }

    @Override
    public Set<Variable> getVariables() {
        return Collections.emptySet();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode("True");
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof TrueExpression;
    }


    @Override
    public String toString() {
        return toStringHelper("True")
                .toString();
    }
}
