package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.builtin.Timestamp;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public class MINUTES_Evaluator extends AbstractUnaryDateTimeBuiltInCallEvaluator {
    @Override
    protected EvaluationResult evaluate(Timestamp timestamp, Expression arg, SolutionMapping sm) {
        return EvaluationResult.getInteger(timestamp.getMinutes());
    }
}
