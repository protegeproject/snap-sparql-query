package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public class ABS_Evaluator extends AbstractUnaryBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Expression arg, SolutionMapping sm) {
        EvaluationResult eval = arg.evaluateAsNumeric(sm);
        if(eval.isError()) {
            return eval;
        }
        Literal literal = eval.asLiteral();
        String lexicalForm = literal.getLexicalForm();
        if(!lexicalForm.startsWith("-")) {
            return eval;
        }
        else {
            Literal absLiteral = new Literal(literal.getDatatype(), literal.getLexicalForm().substring(1), "");
            return EvaluationResult.getResult(absLiteral);
        }
    }
}
