package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class STR_Evaluator implements BuiltInCallEvaluator {

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        if(args.size() != 1) {
            return EvaluationResult.getError();
        }
        EvaluationResult iriEval = args.get(0).evaluateAsIRI(sm);
        if(!iriEval.isError()) {
            EvaluationResult.getResult(new Literal(iriEval.asIRI().getIRI().toString(), ""));
        }
        EvaluationResult literalEval = args.get(0).evaluateAsLiteral(sm);
        if(literalEval.isError()) {
            return literalEval;
        }
        return EvaluationResult.getResult(new Literal(literalEval.asLiteral().getLexicalForm(), ""));
    }
}
