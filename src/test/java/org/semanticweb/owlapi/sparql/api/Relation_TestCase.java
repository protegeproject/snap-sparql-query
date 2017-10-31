package org.semanticweb.owlapi.sparql.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.semanticweb.owlapi.sparql.api.Relation.*;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Oct 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class Relation_TestCase {

    @Mock
    private SolutionMapping solutionMapping;

    @Mock
    private AlgebraEvaluationContext context;

    @Test
    public void shouldBeEqualDecimals() {
        Literal left = Literal.createDecimal(3.0);
        Literal right = Literal.createDecimal(3.0);
        eval(EQUAL, left, right);
    }

    @Test
    public void shouldNotBeEqualDecimals() {
        Literal left = Literal.createDecimal(3.0);
        Literal right = Literal.createDecimal(4.0);
        eval(NOT_EQUAL, left, right);
    }

    @Test
    public void shouldLessThanDecimals() {
        Literal left = Literal.createDecimal(3.0);
        Literal right = Literal.createDecimal(4.0);
        eval(LESS_THAN, left, right);
    }

    @Test
    public void shouldLessThanOrEqualDecimals() {
        Literal left = Literal.createDecimal(3.0);
        Literal right = Literal.createDecimal(4.0);
        eval(LESS_THAN_OR_EQUAL, left, right);
    }

    @Test
    public void shouldLessThanOrEqualEqualDecimals() {
        Literal left = Literal.createDecimal(5.0);
        Literal right = Literal.createDecimal(5.0);
        eval(LESS_THAN_OR_EQUAL, left, right);
    }

    @Test
    public void shouldGreaterThanDecimals() {
        Literal left = Literal.createDecimal(5.0);
        Literal right = Literal.createDecimal(4.0);
        eval(GREATER_THAN, left, right);
    }

    @Test
    public void shouldGreaterThanOrEqualDecimals() {
        Literal left = Literal.createDecimal(5.0);
        Literal right = Literal.createDecimal(4.0);
        eval(GREATER_THAN_OR_EQUAL, left, right);
    }

    @Test
    public void shouldGreaterThanOrEqualEqualDecimals() {
        Literal left = Literal.createDecimal(5.0);
        Literal right = Literal.createDecimal(5.0);
        eval(GREATER_THAN_OR_EQUAL, left, right);
    }


    @Test
    public void shouldBeEqualDateTime() {
        Literal left = new Literal(Datatype.getXSDDateTime(), "2017-09-01T10:55:33Z", "");
        Literal right = new Literal(Datatype.getXSDDateTime(), "2017-09-01T10:55:33Z", "");
        eval(EQUAL, left, right);
    }

    @Test
    public void shouldNotBeEqualDateTime() {
        Literal left = new Literal(Datatype.getXSDDateTime(), "2016-09-01T10:55:33Z", "");
        Literal right = new Literal(Datatype.getXSDDateTime(), "2017-09-01T10:55:33Z", "");
        eval(NOT_EQUAL, left, right);
    }

    @Test
    public void shouldBeLessThanDateTime() {
        Literal left = new Literal(Datatype.getXSDDateTime(), "2016-09-01T10:55:33Z", "");
        Literal right = new Literal(Datatype.getXSDDateTime(), "2017-09-01T10:55:33Z", "");
        eval(LESS_THAN, left, right);
    }

    @Test
    public void shouldBeLessThanOrEqualDateTime() {
        Literal left = new Literal(Datatype.getXSDDateTime(), "2016-09-01T10:55:33Z", "");
        Literal right = new Literal(Datatype.getXSDDateTime(), "2017-09-01T10:55:33Z", "");
        eval(LESS_THAN_OR_EQUAL, left, right);
    }

    @Test
    public void shouldBeGreaterThanDateTime() {
        Literal left = new Literal(Datatype.getXSDDateTime(), "2017-09-01T10:55:33Z", "");
        Literal right = new Literal(Datatype.getXSDDateTime(), "2016-09-01T10:55:33Z", "");
        eval(GREATER_THAN, left, right);
    }

    @Test
    public void shouldBeGreaterThanOrEqualDateTime() {
        Literal left = new Literal(Datatype.getXSDDateTime(), "2017-09-01T10:55:33Z", "");
        Literal right = new Literal(Datatype.getXSDDateTime(), "2016-09-01T10:55:33Z", "");
        eval(GREATER_THAN_OR_EQUAL, left, right);
    }

    @Test
    public void shouldBeEqualBooleans() {
        Literal left = Literal.createBoolean(true);
        Literal right = Literal.createBoolean(true);
        eval(EQUAL, left, right);
    }

    @Test
    public void shouldBeNotEqualBooleans() {
        Literal left = Literal.createBoolean(true);
        Literal right = Literal.createBoolean(false);
        eval(NOT_EQUAL, left, right);
    }

    @Test
    public void shouldBeLessThanBooleans() {
        Literal left = Literal.createBoolean(false);
        Literal right = Literal.createBoolean(true);
        eval(LESS_THAN, left, right);
    }

    @Test
    public void shouldBeLessThanOrEqualBooleans() {
        Literal left = Literal.createBoolean(false);
        Literal right = Literal.createBoolean(true);
        eval(LESS_THAN_OR_EQUAL, left, right);
    }

    @Test
    public void shouldBeLessThanOrEqualEqualBooleans() {
        Literal left = Literal.createBoolean(true);
        Literal right = Literal.createBoolean(true);
        eval(LESS_THAN_OR_EQUAL, left, right);
    }

    @Test
    public void shouldBeGreaterThanBooleans() {
        Literal left = Literal.createBoolean(true);
        Literal right = Literal.createBoolean(false);
        eval(GREATER_THAN, left, right);
    }

    @Test
    public void shouldBeGreaterThanOrEqualBooleans() {
        Literal left = Literal.createBoolean(true);
        Literal right = Literal.createBoolean(false);
        eval(GREATER_THAN_OR_EQUAL, left, right);
    }

    @Test
    public void shouldBeGreaterThanOrEqualEqualBooleans() {
        Literal left = Literal.createBoolean(true);
        Literal right = Literal.createBoolean(true);
        eval(GREATER_THAN_OR_EQUAL, left, right);
    }

    private void eval(Relation relation, Expression left, Expression right) {
        EvaluationResult expected = EvaluationResult.getTrue();
        evalAs(relation, left, right, expected);
    }

    private void evalAs(Relation relation, Expression left, Expression right, EvaluationResult expected) {
        EvaluationResult eval = relation.evaluate(left, right, solutionMapping, context);
        assertThat(eval, is(expected));
    }
}
