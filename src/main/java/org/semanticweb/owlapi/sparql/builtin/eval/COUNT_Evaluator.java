package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.algebra.SolutionSequence;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 23/06/15
 */
public class COUNT_Evaluator implements BuiltInCallEvaluator, BuiltInAggregateCallEvaluator {

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        return EvaluationResult.getError();
    }

    @Override
    public EvaluationResult evaluateAsAggregate(List<Expression> args, SolutionSequence solutionSequence) {
        if(args.size() != 1) {
            return EvaluationResult.getError();
        }
        Expression arg = args.get(0);
        int count = 0;
        for(SolutionMapping sm : solutionSequence.getSolutionMappings()) {
            EvaluationResult eval = arg.evaluate(sm);
            if(!eval.isError()) {
                count++;
            }
        }
        return EvaluationResult.getInteger(count);
    }
}