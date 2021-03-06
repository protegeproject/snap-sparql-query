package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.builtin.DateTime;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public class SECONDS_Evaluator extends AbstractUnaryDateTimeBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(DateTime dateTime, Expression arg, SolutionMapping sm) {
        return EvaluationResult.getDecimal(dateTime.getSeconds());
    }
}
