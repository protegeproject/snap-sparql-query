package org.semanticweb.owlapi.sparql.apiex.builtin;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class STRSTARTS_Evaluator implements BuiltInCallEvaluator {

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        if(args.size() != 2) {
            return EvaluationResult.getError();
        }
        EvaluationResult arg0 = args.get(0).evaluateAsStringLiteral(sm);
        if(arg0.isError()) {
            return arg0;
        }
        EvaluationResult arg1 = args.get(1).evaluateAsStringLiteral(sm);
        if(arg1.isError()) {
            return EvaluationResult.getError();
        }
        return EvaluationResult.getBoolean(arg0.asLiteral().getLexicalForm().startsWith(arg1.asLiteral().getLexicalForm()));
    }
}
