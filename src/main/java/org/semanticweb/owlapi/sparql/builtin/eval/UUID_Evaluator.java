package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.api.AtomicIRI;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public class UUID_Evaluator implements BuiltInCallEvaluator {

    @Nonnull
    @Override
    public EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        UUID uuid = UUID.randomUUID();
        IRI uuidIri = IRI.create("urn:uuid:" + uuid.toString());
        return EvaluationResult.getResult(new AtomicIRI(uuidIri));
    }
}
