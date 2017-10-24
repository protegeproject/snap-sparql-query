package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
public class ISIRI_Evaluator extends AbstractUnaryBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Expression arg, SolutionMapping sm, EvaluationContext evaluationContext) {
        EvaluationResult result = arg.evaluate(sm, evaluationContext);
        if(result.isError()) {
            return result;
        }
        RDFTerm term = result.getResult();
        return EvaluationResult.getBoolean(term instanceof AtomicIRI);
    }
}
