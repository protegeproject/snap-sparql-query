package org.semanticweb.owlapi.sparql.builtin.eval;

import org.apache.commons.lang.WordUtils;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Nov 2016
 */
public class SPLITCAMEL_Evaluator extends AbstractUnaryLiteralBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Literal literal, Expression arg, SolutionMapping sm, EvaluationContext evaluationContext) {
        String lexicalForm = literal.getLexicalForm();
        String nullString = Character.toString((char) 0);
        String withoutUnderscores = lexicalForm.replace("_", nullString);
        String withUnderscores = UPPER_CAMEL.converterTo(LOWER_UNDERSCORE).convert(withoutUnderscores);
        String withSpaces = WordUtils.capitalize(withUnderscores.replace("_", " "));
        String preservedUnderscores = withSpaces.replace(nullString, "_");
        return EvaluationResult.getResult(new Literal(literal.getDatatype(), preservedUnderscores, literal.getLang()));
    }
}
