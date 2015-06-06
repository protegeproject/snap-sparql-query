package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.api.AtomicIRI;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.List;
import java.util.UUID;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public class UUID_Evaluator implements BuiltInCallEvaluator {

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        UUID uuid = UUID.randomUUID();
        IRI uuidIri = IRI.create("urn:uuid:" + uuid.toString());
        return EvaluationResult.getResult(new AtomicIRI(uuidIri));
    }
}
