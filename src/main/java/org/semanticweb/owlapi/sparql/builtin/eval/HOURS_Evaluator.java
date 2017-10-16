package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.builtin.DateTime;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public class HOURS_Evaluator extends AbstractUnaryDateTimeBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(DateTime dateTime, Expression arg, SolutionMapping sm) {
        return EvaluationResult.getInteger(dateTime.getHour());
    }
}
