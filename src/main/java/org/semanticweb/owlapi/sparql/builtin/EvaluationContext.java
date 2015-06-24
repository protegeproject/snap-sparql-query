package org.semanticweb.owlapi.sparql.builtin;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 05/06/15
 */
public class EvaluationContext {

    private long evaluationTime;

    public EvaluationContext(long evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public long getEvaluationTime() {
        return evaluationTime;
    }
}
