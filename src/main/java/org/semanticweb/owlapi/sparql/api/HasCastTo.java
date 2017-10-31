package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Oct 2017
 */
public interface HasCastTo {

    Optional<Literal> castToXSDString();

    Optional<Literal> castToXSDFloat();

    Optional<Literal> castToXSDDouble();

    Optional<Literal> castToXSDDecimal();

    Optional<Literal> castToXSDInteger();

    Optional<Literal> castToXSDDateTime();

    Optional<Literal> castToXSDBoolean();
}

