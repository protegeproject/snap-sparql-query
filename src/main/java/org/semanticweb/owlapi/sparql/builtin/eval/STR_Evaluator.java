package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class STR_Evaluator extends AbstractUnaryBuiltInCallEvaluator {
    @Override
    protected EvaluationResult evaluate(Expression arg, SolutionMapping sm) {
        EvaluationResult literalEval = arg.evaluateAsLiteral(sm);
        if(literalEval.isError()) {
            return literalEval;
        }
        EvaluationResult iriEval = arg.evaluateAsIRI(sm);
        if(!iriEval.isError()) {
            EvaluationResult.getResult(Literal.createRDFPlainLiteral(iriEval.asIRI().getIRI().toString(), ""));
        }
        return EvaluationResult.getResult(Literal.createRDFPlainLiteral(literalEval.asLiteral().getLexicalForm(), ""));
    }

}
