package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.algebra.SolutionSequence;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Oct 2017
 */
public class GROUP_CONCAT_Evaluator implements BuiltInCallEvaluator, BuiltInAggregateCallEvaluator {
    @Override
    public EvaluationResult evaluateAsAggregate(List<Expression> args, SolutionSequence solutionSequence) {
        final Expression arg0;
        final String separator;
        if (args.size() == 1) {
            arg0 = args.get(0);
            separator = " ";
        } else if (args.size() == 2) {
            arg0 = args.get(0);
            Expression arg1 = args.get(1);
            EvaluationResult separatorEval = arg1.evaluateAsSimpleLiteral(SolutionMapping.emptyMapping());
            if (separatorEval.isError()) {
                return EvaluationResult.getError();
            }
            separator = separatorEval.asSimpleLiteral();
        } else {
            return EvaluationResult.getError();
        }
        String concat = solutionSequence.getSolutionMappings().stream()
                .map(arg0::evaluateAsSimpleLiteral)
                .filter(eval -> !eval.isError())
                .map(EvaluationResult::asSimpleLiteral)
                .collect(joining(separator));
        return EvaluationResult.getSimpleLiteral(concat);
    }

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        return EvaluationResult.getError();
    }

    @Override
    public Set<String> getScalars() {
        return Collections.singleton("SEPARATOR");
    }
}
