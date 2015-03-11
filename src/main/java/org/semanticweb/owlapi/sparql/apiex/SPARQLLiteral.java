package org.semanticweb.owlapi.sparql.apiex;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.semanticweb.owlapi.vocab.XSDVocabulary.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/07/2012
 */
public class SPARQLLiteral extends SPARQLRDFTerm {

    private OWLLiteral literal;

    private static final Set<IRI> numericTypes = Collections.unmodifiableSet(new HashSet<IRI>(Arrays.asList(INTEGER.getIRI(),
            DECIMAL.getIRI(),
            FLOAT.getIRI(),
            DOUBLE.getIRI(),
            NON_POSITIVE_INTEGER.getIRI(),
            NEGATIVE_INTEGER.getIRI(),
            LONG.getIRI(),
            SHORT.getIRI(),
            BYTE.getIRI(),
            NON_NEGATIVE_INTEGER.getIRI(),
            UNSIGNED_LONG.getIRI(),
            UNSIGNED_INT.getIRI(),
            UNSIGNED_SHORT.getIRI(),
            UNSIGNED_BYTE.getIRI(),
            POSITIVE_INTEGER.getIRI())));

    public SPARQLLiteral(OWLLiteral literal) {
        this.literal = literal;
    }

    public boolean isPlainLiteral() {
        return literal.isRDFPlainLiteral();
    }

    public boolean isNumeric() {
        return numericTypes.contains(literal.getDatatype().getIRI());
    }
    
    public boolean isSimpleLiteral() {
        return literal.isRDFPlainLiteral() && !literal.hasLang();
    }
    
    public boolean isStringLiteral() {
        return literal.isRDFPlainLiteral() || literal.getDatatype().isString();
    }

    public OWLLiteral getLiteral() {
        return literal;
    }

//    When referring to a type, the following terms denote a typed literal with the corresponding XML Schema [XSDT] datatype IRI:
//
//    xsd:integer
//    xsd:decimal
//    xsd:float
//    xsd:double
//    xsd:string
//    xsd:boolean
//    xsd:dateTime


//    numeric denotes typed literals with datatypes xsd:integer, xsd:decimal, xsd:float, and xsd:double.
//    simple literal denotes a plain literal with no language tag.
//    RDF term denotes the types IRI, literal, and blank node.
//    variable denotes a SPARQL variable.
//
//    The following types are derived from numeric types and are valid arguments to functions and operators taking numeric arguments:
//
//    xsd:nonPositiveInteger
//    xsd:negativeInteger
//    xsd:long
//    xsd:int
//    xsd:short
//    xsd:byte
//    xsd:nonNegativeInteger
//    xsd:unsignedLong
//    xsd:unsignedInt
//    xsd:unsignedShort
//    xsd:unsignedByte
//    xsd:positiveInteger
}
