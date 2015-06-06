package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 05/06/15
 */
public class NOW_Evaluator implements BuiltInCallEvaluator {

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String formatted = formatter.format(new Date(sm.getTime()));
        return EvaluationResult.getResult(new Literal(Datatype.getXSDDateTime(), formatted, ""));
    }
}
