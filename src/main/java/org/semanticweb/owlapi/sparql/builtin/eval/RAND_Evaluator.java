package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public class RAND_Evaluator implements BuiltInCallEvaluator {

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        return EvaluationResult.getDouble(Math.random());
    }
}
