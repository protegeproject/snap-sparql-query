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
public class LANGMATCHES_Evaluator implements BuiltInCallEvaluator {

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        if(args.size() != 2) {
            return EvaluationResult.getError();
        }
        EvaluationResult result0 = args.get(0).evaluateAsStringLiteral(sm);
        if(result0.isError()) {
            return result0;
        }
        EvaluationResult result1 = args.get(1).evaluateAsSimpleLiteral(sm);
        if(result1.isError()) {
            return result1;
        }
        return EvaluationResult.getBoolean(result0.asLiteral().getLang().equalsIgnoreCase(result1.asSimpleLiteral()));
    }
}