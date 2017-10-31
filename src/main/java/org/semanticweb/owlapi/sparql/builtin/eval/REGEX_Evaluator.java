package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class REGEX_Evaluator implements BuiltInCallEvaluator {

    private String lastPattern = "";
    private Pattern patternPattern;

    @Nonnull
    @Override
    public EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if(args.size() != 2 && args.size() != 3) {
            return EvaluationResult.getError();
        }
        Expression string = args.get(0);
        EvaluationResult matchResult = string.evaluate(sm, evaluationContext).asStringLiteralOrElseError();
        if(matchResult.isError()) {
            return matchResult;
        }
        Expression pattern = args.get(1);
        EvaluationResult patternResult = pattern.evaluate(sm, evaluationContext).asSimpleLiteralOrElseError();
        if(patternResult.isError()) {
            return patternResult;
        }
        String regex = patternResult.asSimpleLiteral();
        if(!regex.equals(lastPattern)) {
            lastPattern = regex;
            patternPattern = Pattern.compile(regex);
        }
        return EvaluationResult.getBoolean(patternPattern.matcher(matchResult.asLiteral().getLexicalForm()).find());
    }
}
