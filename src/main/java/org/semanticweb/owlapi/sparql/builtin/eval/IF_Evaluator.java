package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public class IF_Evaluator implements BuiltInCallEvaluator {

    @Nonnull
    @Override
    public EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm) {
        if(args.size() != 3) {
            return EvaluationResult.getError();
        }
        Expression arg0 = args.get(0);
        Expression arg1 = args.get(1);
        Expression arg2 = args.get(2);
        EvaluationResult eval0 = arg0.evaluateAsEffectiveBooleanValue(sm);
        if(eval0.isTrue()) {
            return arg1.evaluate(sm);
        }
        else {
            return arg2.evaluate(sm);
        }
    }
}
