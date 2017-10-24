package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.EvaluationContext;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
public class CEIL_Evaluator extends AbstractUnaryBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Expression arg, SolutionMapping sm, EvaluationContext evaluationContext) {
        EvaluationResult eval = arg.evaluateAsNumeric(sm, evaluationContext);
        if(eval.isError()) {
            return EvaluationResult.getError();
        }
        double numericValue = eval.asNumeric();
        double ceiling = Math.ceil(numericValue);
        return new EvaluationResult(Literal.createDecimal(ceiling));
    }
}
