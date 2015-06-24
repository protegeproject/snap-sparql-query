package org.semanticweb.owlapi.sparql.algebra;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Variable;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 23/06/15
 */
public class AggregationEvaluation {

    private final GroupKey groupKey;

    private final Variable variable;

    private final EvaluationResult evaluationResult;

    public AggregationEvaluation(GroupKey groupKey, Variable variable, EvaluationResult evaluationResult) {
        this.groupKey = groupKey;
        this.variable = variable;
        this.evaluationResult = evaluationResult;
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
}
