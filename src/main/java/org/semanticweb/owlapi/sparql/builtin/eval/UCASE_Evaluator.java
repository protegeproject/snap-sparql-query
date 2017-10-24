package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.EvaluationContext;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class UCASE_Evaluator implements BuiltInCallEvaluator {

    @Nonnull
    @Override
    public EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm, EvaluationContext evaluationContext) {
        if(args.size() != 1) {
            return EvaluationResult.getError();
        }
        EvaluationResult result1 = args.get(0).evaluateAsStringLiteral(sm, evaluationContext);
        if(result1.isError()) {
            return EvaluationResult.getError();
        }
        Literal literalArg = result1.asLiteral();
        Literal literal = new Literal(literalArg.getDatatype(), literalArg.getLexicalForm().toUpperCase(), literalArg.getLang());
        return EvaluationResult.getResult(literal);
    }
}
