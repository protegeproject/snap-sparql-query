package org.semanticweb.owlapi.sparql.api;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 12/06/15
 */
public interface RDFTerm extends HasEvaluation, AnnotationValue, HasCastTo {

    /**
     * Determines if this term is numeric.  To be numeric the term should be a {@link Literal} with
     * a numeric datatype and a value that is in the lexical space of the datatype.
     */
    boolean isNumeric();

    /**
     * Determines if this term is a dateTime.  To be a dateTime the term should be a {@link Literal} with
     * a datatype of xsd:dateTime and a value that is in the lexical space of this datatype.
     */
    boolean isXSDDateTime();

    /**
     * Determines if this term is an xsd:string.  To be an xsd:string the term should be a {@link Literal} with
     * a datatype of xsd:string.
     */
    boolean isXSDString();

    /**
     * Determines if this term is a string literal.  A string literal is a {@link Literal} with a datatype of
     * xsd:string, a plain literal with a language tag, or a plain literal without a language tag (simple literal)
     */
    boolean isStringLiteral();


    /**
     * Determines if this term is a simple literal.  A simple literal is a {@link Literal} with a datatype of
     * rdf:PlainLiteral and without a language tag.
     */
    boolean isSimpleLiteral();


}
