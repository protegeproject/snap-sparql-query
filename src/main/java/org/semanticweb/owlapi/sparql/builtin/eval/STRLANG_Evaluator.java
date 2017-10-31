package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 07/06/15
 */
public class STRLANG_Evaluator implements BuiltInCallEvaluator {

    @Nonnull
    @Override
    public EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if(args.size() != 2) {
            return EvaluationResult.getError();
        }
        EvaluationResult lexicalFormEval = args.get(0).evaluate(sm, evaluationContext).asStringLiteralOrElseError();
        if(lexicalFormEval.isError()) {
            return EvaluationResult.getError();
        }
        EvaluationResult langTagEval = args.get(1).evaluate(sm, evaluationContext).asStringLiteralOrElseError();
        if(langTagEval.isError()) {
            return EvaluationResult.getError();
        }
        Literal result = new Literal(Datatype.getRDFPlainLiteral(), lexicalFormEval.asSimpleLiteral(), langTagEval.asSimpleLiteral());
        return EvaluationResult.getResult(result);
    }
}
