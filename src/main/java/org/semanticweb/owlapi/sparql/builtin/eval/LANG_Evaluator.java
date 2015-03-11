package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class LANG_Evaluator extends AbstractUnaryStringLiteralBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Literal literal, SolutionMapping sm) {
        String lang = literal.getLang();
        return EvaluationResult.getResult(new Literal(Datatype.getXSDString(), lang));
    }
}
