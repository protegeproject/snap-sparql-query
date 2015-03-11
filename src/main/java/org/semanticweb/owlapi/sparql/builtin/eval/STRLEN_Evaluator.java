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
public class STRLEN_Evaluator implements BuiltInCallEvaluator {

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        EvaluationResult eval = args.get(0).evaluateAsStringLiteral(sm);
        if(eval.isError()) {
            return eval;
        }
        else {
            return EvaluationResult.getInteger(eval.asLiteral().getLexicalForm().length());
        }
    }
}
