package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.algebra.SolutionSequence;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;

import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 23/06/15
 */
public interface BuiltInAggregateCallEvaluator {

    EvaluationResult evaluateAsAggregate(List<Expression> args, SolutionSequence solutionSequence);
}
