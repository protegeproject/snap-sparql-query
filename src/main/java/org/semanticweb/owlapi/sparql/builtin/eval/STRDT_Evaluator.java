package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.algebra.EvaluationContext;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 07/06/15
 */
public class STRDT_Evaluator implements BuiltInCallEvaluator {

    @Nonnull
    @Override
    public EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm, EvaluationContext evaluationContext) {
        if(args.size() != 2) {
            return EvaluationResult.getError();
        }
        EvaluationResult eval0 = args.get(0).evaluateAsSimpleLiteral(sm, evaluationContext);
        if(eval0.isError()) {
            return EvaluationResult.getError();
        }
        EvaluationResult eval1 = args.get(1).evaluateAsIRI(sm, evaluationContext);
        if(eval1.isError()) {
            return EvaluationResult.getError();
        }
        String lexicalForm = eval0.asSimpleLiteral();
        AtomicIRI iri = eval1.asIRI();
        Literal result = new Literal(new Datatype(iri.getIRI()), lexicalForm, "");
        return EvaluationResult.getResult(result);
    }
}
