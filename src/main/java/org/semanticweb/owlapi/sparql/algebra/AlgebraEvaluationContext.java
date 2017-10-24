package org.semanticweb.owlapi.sparql.algebra;

import org.semanticweb.owlapi.sparql.sparqldl.BgpEvaluator;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 11/06/15
 */
public class AlgebraEvaluationContext {

    private BgpEvaluator bgpEvaluator;

    private final ZonedDateTime evaluationTime;

    public AlgebraEvaluationContext(BgpEvaluator bgpEvaluator, ZonedDateTime evaluationTime) {
        this.bgpEvaluator = checkNotNull(bgpEvaluator);
        this.evaluationTime = checkNotNull(evaluationTime);
    }

    public SolutionSequence evaluateBgp(Bgp bgp) {
        return bgpEvaluator.evaluate(bgp);
    }


    @Nonnull
    public ZonedDateTime getEvaluationTime() {
        return evaluationTime;
    }
}

