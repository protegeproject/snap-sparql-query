package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 26/03/15
 */
public class IRI_Evaluator extends AbstractUnaryBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Expression arg, SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        EvaluationResult result = arg.evaluate(sm, evaluationContext);
        if(result.isError()) {
            return EvaluationResult.getError();
        }
        RDFTerm term = result.getResult();
        if(term instanceof AtomicIRI) {
            return result;
        }
        if(term instanceof Literal) {
            Literal literal = (Literal) term;
            if (literal.isSimpleLiteral() || literal.isXSDString()) {
                return EvaluationResult.getResult(AtomicIRI.create(literal.getLexicalForm()));
            }
        }
        return EvaluationResult.getError();
    }
}
