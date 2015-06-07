package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.*;

import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 07/06/15
 */
public class STRLANG_Evaluator implements BuiltInCallEvaluator {

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        if(args.size() != 2) {
            return EvaluationResult.getError();
        }
        EvaluationResult lexicalFormEval = args.get(0).evaluateAsSimpleLiteral(sm);
        if(lexicalFormEval.isError()) {
            return EvaluationResult.getError();
        }
        EvaluationResult langTagEval = args.get(1).evaluateAsSimpleLiteral(sm);
        if(langTagEval.isError()) {
            return EvaluationResult.getError();
        }
        Literal result = new Literal(Datatype.getRDFPlainLiteral(), lexicalFormEval.asSimpleLiteral(), langTagEval.asSimpleLiteral());
        return EvaluationResult.getResult(result);
    }
}
