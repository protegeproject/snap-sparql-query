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
     * Determines if this term is a literal that does not have a lang tag or has an empty lang tag (rdf:PlainLiteral or otherwise)
     */
    boolean isLiteralWithoutLangTagOrWithEmptyLangTag();

    /**
     * @return Returns true if this term is a Literal and, it is either an rdf:PlainLiteral, or
     * it is a sub-type of rdf:PlainLiteral (xsd:string and subtypes, rdf:langString), or it is xsd:anyURI (which is
     * promotable to an xsd:string according to the xpath spec). Returns false anything else.
     *
     */
    boolean isSubTypeOfOrPromotableToRdfPlainLiteral();

    /**
     * Determines if this term is an xsd:boolean.  To be an xsd:boolean the term should be a {@link Literal} with
     * a datatype of xsd:boolean.
     */
    boolean isXSDBoolean();

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
