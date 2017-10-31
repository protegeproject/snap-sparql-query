package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class STR_Evaluator extends AbstractUnaryBuiltInCallEvaluator {
    @Override
    protected EvaluationResult evaluate(Expression arg, SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        EvaluationResult literalEval = arg.evaluate(sm, evaluationContext);
        RDFTerm term = literalEval.getResult();
        if(term instanceof Literal) {
            return EvaluationResult.getSimpleLiteral(literalEval.asSimpleLiteral());
        }
        else if(term instanceof AtomicIRI) {
            return EvaluationResult.getSimpleLiteral(((AtomicIRI) term).getIdentifier());
        }
        else {
            return EvaluationResult.getError();
        }
    }

}
