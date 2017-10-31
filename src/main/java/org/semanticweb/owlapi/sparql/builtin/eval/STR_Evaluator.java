package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class STR_Evaluator extends AbstractUnaryBuiltInCallEvaluator {
    @Override
    protected EvaluationResult evaluate(Expression arg, SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        EvaluationResult literalEval = arg.evaluate(sm, evaluationContext).asLiteralOrElseError();
        if(literalEval.isError()) {
            return literalEval;
        }
        EvaluationResult iriEval = arg.evaluate(sm, evaluationContext).asIriOrElseError();
        if(!iriEval.isError()) {
            EvaluationResult.getResult(Literal.createRDFPlainLiteral(iriEval.asIRI().getIRI().toString(), ""));
        }
        return EvaluationResult.getResult(Literal.createRDFPlainLiteral(literalEval.asLiteral().getLexicalForm(), ""));
    }

}
