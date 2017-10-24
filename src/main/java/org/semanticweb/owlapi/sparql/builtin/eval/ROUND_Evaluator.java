package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.algebra.EvaluationContext;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public class ROUND_Evaluator extends AbstractUnaryBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Expression arg, SolutionMapping sm, EvaluationContext evaluationContext) {
        EvaluationResult eval = arg.evaluateAsNumeric(sm, evaluationContext);
        if(eval.isError()) {
            return eval;
        }
        Literal literal = eval.asLiteral();
        if(literal.getDatatype().equals(Datatype.getXSDInteger())) {
            return eval;
        }
        else if(literal.getDatatype().equals(Datatype.getXSDFloat())) {
            return EvaluationResult.getFloat(Math.round((float) eval.asNumeric()));
        }
        else if(literal.getDatatype().equals(Datatype.getXSDDecimal())) {
            return EvaluationResult.getDecimal(Math.round(eval.asNumeric()));
        }
        else {
            double value = eval.asNumeric();
            return EvaluationResult.getLong(Math.round(value));
        }

    }
}
