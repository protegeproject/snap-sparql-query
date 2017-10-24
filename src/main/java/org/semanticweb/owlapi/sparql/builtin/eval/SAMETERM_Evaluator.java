package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class SAMETERM_Evaluator implements BuiltInCallEvaluator {

    @Nonnull
    @Override
    public EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if(args.size() != 2) {
            return EvaluationResult.getError();
        }
        else {
            EvaluationResult result1 = args.get(0).evaluate(sm, evaluationContext);
            if(result1.isError()) {
                return EvaluationResult.getError();
            }
            EvaluationResult result2 = args.get(1).evaluate(sm, evaluationContext);
            if(result2.isError()) {
                return EvaluationResult.getError();
            }
            return EvaluationResult.getBoolean(result1.getResult().equals(result2.getResult()));
        }
    }
}
