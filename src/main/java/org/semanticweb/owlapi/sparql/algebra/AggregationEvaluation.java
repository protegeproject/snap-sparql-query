package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Variable;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 23/06/15
 */
public class AggregationEvaluation {

    private final GroupKey groupKey;

    private final Variable variable;

    private final EvaluationResult evaluationResult;

    public AggregationEvaluation(GroupKey groupKey, Variable variable, EvaluationResult evaluationResult) {
        this.groupKey = checkNotNull(groupKey);
        this.variable = checkNotNull(variable);
        this.evaluationResult = checkNotNull(evaluationResult);
    }



    public GroupKey getGroupKey() {
        return groupKey;
    }

    public Variable getVariable() {
        return variable;
    }

    public EvaluationResult getEvaluationResult() {
        return evaluationResult;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(groupKey, variable, evaluationResult);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AggregationEvaluation)) {
            return false;
        }
        AggregationEvaluation other = (AggregationEvaluation) obj;
        return this.groupKey.equals(other.groupKey)
                && this.variable.equals(other.variable)
                && this.evaluationResult.equals(other.evaluationResult);
    }


    @Override
    public String toString() {
        return toStringHelper("AggregationEvaluation")
                .addValue(groupKey)
                .addValue(variable)
                .addValue(evaluationResult)
                .toString();
    }
}
