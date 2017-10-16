package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.*;

import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
public class ISIRI_Evaluator extends AbstractUnaryBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Expression arg, SolutionMapping sm) {
        EvaluationResult result = arg.evaluate(sm);
        if(result.isError()) {
            return result;
        }
        RDFTerm term = result.getResult();
        return EvaluationResult.getBoolean(term instanceof AtomicIRI);
    }
}
