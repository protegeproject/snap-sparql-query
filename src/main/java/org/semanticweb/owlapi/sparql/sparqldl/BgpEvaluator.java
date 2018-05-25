package org.semanticweb.owlapi.sparql.sparqldl;

import com.google.common.base.Stopwatch;
import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableList;
import de.derivo.sparqldlapi.Query;
import de.derivo.sparqldlapi.QueryEngine;
import de.derivo.sparqldlapi.QueryResult;
import de.derivo.sparqldlapi.exceptions.QueryEngineException;
import de.derivo.sparqldlapi.impl.QueryEngineImpl;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.sparql.algebra.Bgp;
import org.semanticweb.owlapi.sparql.algebra.SolutionSequence;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 11/06/15
 */
public class BgpEvaluator {

    private static final Logger logger = LoggerFactory.getLogger(BgpEvaluator.class);

    private Cache<Bgp, SolutionSequence> cache;

    private OWLDataFactory dataFactory;

    private QueryEngine queryEngine;

    public BgpEvaluator(@Nonnull OWLDataFactory dataFactory,
                        @Nonnull Cache<Bgp, SolutionSequence> cache,
                        @Nonnull QueryEngine queryEngine) {
        this.dataFactory = checkNotNull(dataFactory);
        this.cache = checkNotNull(cache);
        this.queryEngine = checkNotNull(queryEngine);
    }

    public SolutionSequence evaluate(Bgp originalBgp) {
        Bgp simplifiedBgp = originalBgp.getSimplified();
        Stopwatch stopwatch = Stopwatch.createStarted();
//        SolutionSequence cached = cache.getIfPresent(simplifiedBgp);
//        if(cached != null) {
//            logger.info("[Snap SPARQL Bgp Evaluator] Using cached results for BGP: {}", simplifiedBgp);
//            return cached;
//        }
        try {
            BgpTranslator bgpTranslator = new BgpTranslator(dataFactory);
            Query query = bgpTranslator.translate(simplifiedBgp);
            logger.info("[Snap SPARQL Bgp Evaluator] Evaluating BGP: {}", simplifiedBgp);
            ((QueryEngineImpl) queryEngine).setPerformArgumentChecking(false);
            QueryResult result = queryEngine.execute(query);
            ResultTranslator resultTranslator = new ResultTranslator(new SolutionMappingTranslator(), simplifiedBgp.getVariables());
            ImmutableList<SolutionMapping> solutionMappings = resultTranslator.translateResult(result);
            SolutionSequence sequence = new SolutionSequence(new ArrayList<>(simplifiedBgp.getVariables()), solutionMappings);
            logger.info("[Snap SPARQL Bgp Evaluator] Evaluated BGP in {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
            cache.put(simplifiedBgp, sequence);
            return sequence;
        } catch (QueryEngineException e) {
            throw new RuntimeException(e);
        } finally {
            stopwatch.stop();

        }
    }
}
