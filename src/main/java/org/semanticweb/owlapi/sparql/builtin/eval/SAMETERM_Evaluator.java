package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class SAMETERM_Evaluator implements BuiltInCallEvaluator {

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        if(args.size() != 2) {
            return EvaluationResult.getError();
        }
        else {
//            EvaluationResult result1 = args.get(0).evaluate(sm);
//            if(result1.isError()) {
//                return result1;
//            }
//            EvaluationResult result2 = args.get(1).evaluate(sm);
//            if(result2.isError()) {
//                return result2;
//            }
//            return EvaluationResult.getBoolean(result1.getResult().equals(result2.getResult()));
            throw new RuntimeException("TODO");
        }
    }
}
