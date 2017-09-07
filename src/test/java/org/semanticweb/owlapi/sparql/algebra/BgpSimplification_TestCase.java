package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import org.junit.*;
import org.junit.Test;
import org.semanticweb.owlapi.sparql.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Sep 2017
 */
public class BgpSimplification_TestCase {

    private Bgp bgp;
    private Declaration subjectDecl;
    private Declaration objectDecl;
    private AnnotationAssertion annotationAssertion;
    private Declaration properyDecl;

    @Before
    public void setUp() throws Exception {
        List<Axiom> axioms = new ArrayList<>();
        ClassVariable s = new ClassVariable("s");
        subjectDecl = new Declaration(s);
        axioms.add(subjectDecl);
        AnnotationPropertyVariable p = new AnnotationPropertyVariable("p");
        properyDecl = new Declaration(p);
        axioms.add(properyDecl);
        AnnotationValueVariable o = new AnnotationValueVariable("o");
        objectDecl = new Declaration(o);
        axioms.add(objectDecl);
        annotationAssertion = new AnnotationAssertion(p, new IRIVariable("s"), o);
        axioms.add(annotationAssertion);
        bgp = new Bgp(ImmutableList.copyOf(axioms));
    }

    @Test
    public void shouldSimplifyContainingAnnotationAssertion() {
        Bgp simplifiedBgp = bgp.getSimplified();
        assertThat(simplifiedBgp.getAxioms(), hasItem(annotationAssertion));
    }

    @Test
    public void shouldSimplifyContainingClassDeclaration() {
        Bgp simplifiedBgp = bgp.getSimplified();
        assertThat(simplifiedBgp.getAxioms(), hasItem(subjectDecl));
    }

    @Test
    public void shouldSimplifyContainingValueDeclaration() {
        Bgp simplifiedBgp = bgp.getSimplified();
        assertThat(simplifiedBgp.getAxioms(), hasItem(objectDecl));
    }

    @Test
    public void shouldSimplifyNotContainingPropertyDeclaration() {
        Bgp simplifiedBgp = bgp.getSimplified();
        assertThat(simplifiedBgp.getAxioms(), not(hasItem(properyDecl)));
    }
}

