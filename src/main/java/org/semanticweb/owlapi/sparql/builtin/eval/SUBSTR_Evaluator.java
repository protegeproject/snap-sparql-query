package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public class SUBSTR_Evaluator implements BuiltInCallEvaluator {

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        if(args.size() < 2 || args.size() > 3) {
            return EvaluationResult.getError();
        }
        Expression arg0;
        Expression arg1;
        arg0 = args.get(0);
        EvaluationResult arg0Eval = arg0.evaluateAsLiteral(sm);
        if(arg0Eval.isError()) {
            return arg0Eval;
        }
        Literal literal = arg0Eval.asLiteral();
        String lexicalForm = literal.getLexicalForm();

        arg1 = args.get(1);
        EvaluationResult arg1Eval = arg1.evaluateAsNumeric(sm);
        if(arg1Eval.isError()) {
            return arg0Eval;
        }

        int startIndex = (int) arg1Eval.asNumeric();
        int length = lexicalForm.length();
        if(args.size() == 3) {
            EvaluationResult arg2Eval = args.get(2).evaluateAsNumeric(sm);
            if(arg2Eval.isError()) {
                return arg2Eval;
            }
            length = (int) arg2Eval.asNumeric();
        }
        if(startIndex + length > lexicalForm.length()) {
            return EvaluationResult.getError();
        }
        String substring = lexicalForm.substring(startIndex, startIndex + length);
        return EvaluationResult.getResult(new Literal(literal.getDatatype(), substring, literal.getLang()));
    }
}
