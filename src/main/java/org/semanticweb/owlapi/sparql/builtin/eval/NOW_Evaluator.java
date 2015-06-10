package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.builtin.Timestamp;

import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 05/06/15
 */
public class NOW_Evaluator implements BuiltInCallEvaluator {

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        return EvaluationResult.getResult(new Literal(Datatype.getXSDDateTime(), ts.getFormattedDateTime(), ""));
    }
}
