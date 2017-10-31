package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public class SUBSTR_Evaluator implements BuiltInCallEvaluator {


    private static final int MIN_ARGS_LENGTH = 2;

    private static final int MAX_ARGS_LENGTH = 3;

    private static final int STRING_ARG_INDEX = 0;

    private static final int START_INDEX_INDEX = 1;

    private static final int LENGTH_ARG_INDEX = 2;


    @Nonnull
    @Override
    public EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if(args.size() < MIN_ARGS_LENGTH || args.size() > MAX_ARGS_LENGTH) {
            return EvaluationResult.getError();
        }
        final EvaluationResult stringArgEval = args.get(STRING_ARG_INDEX).evaluateAsLiteral(sm, evaluationContext);
        if(stringArgEval.isError()) {
            return EvaluationResult.getError();
        }

        final EvaluationResult startIndexArgEval = args.get(START_INDEX_INDEX).evaluate(sm, evaluationContext).asNumericOrElseError();
        if(startIndexArgEval.isError()) {
            return EvaluationResult.getError();
        }

        Literal literal = stringArgEval.asLiteral();
        String lexicalForm = literal.getLexicalForm();

        int startIndex = (int) startIndexArgEval.asNumeric();
        final String substring;
        if(argsContainsLengthArg(args)) {
            EvaluationResult lengthArgEval = args.get(LENGTH_ARG_INDEX).evaluate(sm, evaluationContext).asNumericOrElseError();
            if(lengthArgEval.isError()) {
                return EvaluationResult.getError();
            }
            int length = (int) lengthArgEval.asNumeric();
            if(startIndex + length > lexicalForm.length()) {
                return EvaluationResult.getError();
            }
            substring = lexicalForm.substring(startIndex, startIndex + length);
        }
        else {
            substring = lexicalForm.substring(startIndex);
        }
        return EvaluationResult.getResult(new Literal(literal.getDatatype(), substring, literal.getLang()));
    }

    private static boolean argsContainsLengthArg(List<Expression> args) {
        return args.size() == 3;
    }
}
