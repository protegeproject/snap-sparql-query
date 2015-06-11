package org.semanticweb.owlapi.sparql.sparqldl;

import com.google.common.base.Stopwatch;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import de.derivo.sparqldlapi.Query;
import de.derivo.sparqldlapi.QueryEngine;
import de.derivo.sparqldlapi.QueryResult;
import de.derivo.sparqldlapi.exceptions.QueryEngineException;
import de.derivo.sparqldlapi.impl.LiteralTranslator;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.sparql.algebra.Bgp;
import org.semanticweb.owlapi.sparql.algebra.SolutionSequence;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 11/06/15
 */
public class BgpEvaluator {

    private Cache<Bgp, SolutionSequence> cache;

    private OWLReasoner reasoner;

    public BgpEvaluator(OWLReasoner reasoner, Cache<Bgp, SolutionSequence> cache) {
        this.reasoner = reasoner;
        this.cache = cache;
    }

    public SolutionSequence evaluate(Bgp bgp) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        SolutionSequence cached = cache.getIfPresent(bgp);
        if(cached != null) {
            return cached;
        }
        try {
            OWLOntology rootOntology = reasoner.getRootOntology();
            OWLOntologyManager manager = rootOntology.getOWLOntologyManager();
            BgpTranslator bgpTranslator = new BgpTranslator();
            Query query = bgpTranslator.translate(bgp);
            QueryEngine posQE = QueryEngine.create(manager, reasoner);
            QueryResult result = posQE.execute(query);
            LiteralTranslator literalTranslator = new LiteralTranslator(manager.getOWLDataFactory());
            ResultTranslator resultTranslator = new ResultTranslator(new SolutionMappingTranslator(literalTranslator), literalTranslator, bgp.getVariables());
            ImmutableList<SolutionMapping> solutionMappings = resultTranslator.translateResult(result);
            SolutionSequence sequence = new SolutionSequence(new ArrayList<>(bgp.getVariables()), solutionMappings);
            cache.put(bgp, sequence);
            return sequence;
        } catch (QueryEngineException e) {
            throw new RuntimeException();
        } finally {
            stopwatch.stop();
            System.out.println("Evaluated BGP in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        }
    }
}
