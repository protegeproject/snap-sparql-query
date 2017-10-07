package org.semanticweb.owlapi.sparql.api;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 6 Oct 2017
 */
public class SPARQLPrefixManager_TestCase {

    private SPARQLPrefixManager pm;

    @Before
    public void setUp() throws Exception {
        pm = new SPARQLPrefixManager();
        pm.setPrefix(":", "http://www.stuff.com/");
        pm.setPrefix("prefixA:", "http://other.org/prefixes/");
        pm.setPrefix("prefixB:", "http://other.com/prefixes/");
        pm.setPrefix("longer:", "http://www.stuff.com/etc/");
        pm.setPrefix("shorter:", "http://www.stuff");
    }

    @Test
    public void shouldGetPrefixedNameForIri() {
        assertThat(pm.getPrefixedNameOrIri(IRI.create("http://www.stuff.com/A")), is(":A"));
        assertThat(pm.getPrefixedNameOrIri(IRI.create("http://other.org/prefixes/X")), is("prefixA:X"));
        assertThat(pm.getPrefixedNameOrIri(IRI.create("http://other.com/prefixes/Y")), is("prefixB:Y"));
    }

    @Test
    public void shouldContainSetPrefixName() {
        assertThat(pm.containsPrefixMapping(":"), is(true));
    }

    @Test
    public void shouldGetQuotedIriForUnknowPrefixName() {
        String iriString = "http://other.com/A";
        assertThat(pm.getPrefixedNameOrIri(IRI.create(iriString)), is("<" + iriString + ">"));
    }

    @Test
    public void shouldGetIriForPrefixedName() {
        assertThat(pm.getIRI(":A"), is(IRI.create("http://www.stuff.com/A")));
    }
}