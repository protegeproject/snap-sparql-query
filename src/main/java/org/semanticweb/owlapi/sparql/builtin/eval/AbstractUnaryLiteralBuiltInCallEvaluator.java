package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public abstract class AbstractUnaryLiteralBuiltInCallEvaluator extends AbstractUnaryBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Expression arg, SolutionMapping sm) {
        EvaluationResult eval = arg.evaluateAsLiteral(sm);
        if(eval.isError()) {
            return EvaluationResult.getError();
        }
        else {
            return evaluate(eval.asLiteral(), arg, sm);
        }
    }

    protected abstract EvaluationResult evaluate(Literal literal, Expression arg, SolutionMapping sm);
}
