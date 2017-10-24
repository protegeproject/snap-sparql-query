package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public abstract class AbstractUnaryLiteralBuiltInCallEvaluator extends AbstractUnaryBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Expression arg, SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        EvaluationResult eval = arg.evaluateAsLiteral(sm, evaluationContext);
        if(eval.isError()) {
            return EvaluationResult.getError();
        }
        else {
            return evaluate(eval.asLiteral(), arg, sm, evaluationContext);
        }
    }

    protected abstract EvaluationResult evaluate(Literal literal, Expression arg, SolutionMapping sm, AlgebraEvaluationContext evaluationContext);
}
