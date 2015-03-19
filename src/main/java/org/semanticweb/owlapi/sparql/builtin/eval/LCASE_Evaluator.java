package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class LCASE_Evaluator extends AbstractUnaryStringLiteralBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Literal literal, SolutionMapping sm) {
        String lit = literal.getLexicalForm();
        return EvaluationResult.getResult(Literal.createRDFPlainLiteral(lit.toLowerCase(), literal.getLang()));
    }
}
