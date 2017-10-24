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

    public AlgebraEvaluationContext(@Nonnull BgpEvaluator bgpEvaluator,
                                    @Nonnull ZonedDateTime evaluationTime) {
        this.bgpEvaluator = checkNotNull(bgpEvaluator);
        this.evaluationTime = checkNotNull(evaluationTime);
    }

    public SolutionSequence evaluateBgp(Bgp bgp) {
        return bgpEvaluator.evaluate(bgp);
    }


    /**
     * Gets the time of the current evaluation.
     */
    @Nonnull
    public ZonedDateTime getEvaluationTime() {
        return evaluationTime;
    }
}

