package org.semanticweb.owlapi.sparql.algebra;

import org.semanticweb.owlapi.sparql.sparqldl.BgpEvaluator;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 11/06/15
 */
public class AlgebraEvaluationContext {

    private BgpEvaluator bgpEvaluator;

    public AlgebraEvaluationContext(BgpEvaluator bgpEvaluator) {
        this.bgpEvaluator = bgpEvaluator;
    }

    public SolutionSequence evaluateBgp(Bgp bgp) {
        return bgpEvaluator.evaluate(bgp);
    }
}

