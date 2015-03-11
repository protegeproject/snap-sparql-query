package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class CONTAINS_Evaluator implements BuiltInCallEvaluator {

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
            return arg1;
        }
        boolean b = arg0.asLiteral().getLexicalForm().contains(arg1.asLiteral().getLexicalForm());
        return EvaluationResult.getBoolean(b);
    }
}
