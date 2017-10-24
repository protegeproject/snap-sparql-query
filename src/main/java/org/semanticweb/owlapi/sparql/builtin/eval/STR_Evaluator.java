package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class STR_Evaluator extends AbstractUnaryBuiltInCallEvaluator {
    @Override
    protected EvaluationResult evaluate(Expression arg, SolutionMapping sm, EvaluationContext evaluationContext) {
        EvaluationResult literalEval = arg.evaluateAsLiteral(sm, evaluationContext);
        if(literalEval.isError()) {
            return literalEval;
        }
        EvaluationResult iriEval = arg.evaluateAsIRI(sm, evaluationContext);
        if(!iriEval.isError()) {
            EvaluationResult.getResult(Literal.createRDFPlainLiteral(iriEval.asIRI().getIRI().toString(), ""));
        }
        return EvaluationResult.getResult(Literal.createRDFPlainLiteral(literalEval.asLiteral().getLexicalForm(), ""));
    }

}
