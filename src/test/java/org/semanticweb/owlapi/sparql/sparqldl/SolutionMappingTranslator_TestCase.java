
package org.semanticweb.owlapi.sparql.sparqldl;

import java.util.Optional;
import de.derivo.sparqldlapi.QueryArgument;
import de.derivo.sparqldlapi.QueryBinding;
import de.derivo.sparqldlapi.Var;
import de.derivo.sparqldlapi.impl.LiteralTranslator;
import de.derivo.sparqldlapi.types.QueryArgumentType;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.api.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class SolutionMappingTranslator_TestCase {

    public static final String VAR_NAME = "x";
    private SolutionMappingTranslator solutionMappingTranslator;

    @Mock
    private LiteralTranslator literalTranslator;

    @Mock
    private QueryBinding queryBinding;

    private Map<String, Variable> nameMap;

    @Mock
    private QueryArgument boundVariable, boundValue;

    @Mock
    private Variable var;

    @Mock
    private RDFTerm boundTerm;

    @Before
    public void setUp() {
        solutionMappingTranslator = new SolutionMappingTranslator();
        nameMap = new HashMap<>();

        when(boundVariable.getValueAsVar()).thenReturn(new Var(VAR_NAME));
        when(boundVariable.getType()).thenReturn(QueryArgumentType.VAR);

        when(queryBinding.getBoundArgs()).thenReturn(Collections.singleton(boundVariable));

        when(queryBinding.get(boundVariable)).thenReturn(boundValue);

        when(var.getName()).thenReturn(VAR_NAME);
        when(var.getBound(any(IRI.class))).thenReturn(boundTerm);

        nameMap.put(VAR_NAME, var);
    }

    @Test
    public void should_translateBoundArg() {
        IRI iri = IRI.create("http://stuff.com/A");

        when(boundValue.getValueAsIRI()).thenReturn(iri);
        when(boundValue.getType()).thenReturn(QueryArgumentType.URI);

        SolutionMapping solutionMapping = solutionMappingTranslator.translate(queryBinding, nameMap);
        verify(var, times(1)).getBound(iri);
        assertThat(solutionMapping.getTermForVariable(var), is(Optional.of(boundTerm)));
    }

}
