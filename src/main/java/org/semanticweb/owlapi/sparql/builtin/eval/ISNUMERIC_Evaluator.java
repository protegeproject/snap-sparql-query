package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.algebra.EvaluationContext;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
public class ISNUMERIC_Evaluator extends AbstractUnaryBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Expression arg, SolutionMapping sm, EvaluationContext evaluationContext) {
        EvaluationResult result = arg.evaluateAsLiteral(sm, evaluationContext);
        if(result.isError()) {
            return EvaluationResult.getFalse();
        }
        Literal literal = result.asLiteral();
        return EvaluationResult.getBoolean(literal.isDatatypeNumeric());
    }
}
