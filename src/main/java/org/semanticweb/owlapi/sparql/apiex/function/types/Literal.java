package org.semanticweb.owlapi.sparql.apiex.function.types;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.semanticweb.owlapi.vocab.XSDVocabulary.*;
import static org.semanticweb.owlapi.vocab.XSDVocabulary.POSITIVE_INTEGER;
import static org.semanticweb.owlapi.vocab.XSDVocabulary.UNSIGNED_BYTE;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2012
 */
public class Literal extends FunctionOperand {

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

    private OWLLiteral literal;

    public Literal(OWLLiteral literal) {
        this.literal = literal;
    }


    public boolean isRDFPlainLiteral() {
        return literal.isRDFPlainLiteral();
    }
    
    public String getLang() {
        return literal.getLang();
    }

    public boolean hasLang() {
        return literal.hasLang();
    }

    public String getLexicalValue() {
        return literal.getLiteral();
    }
    
    public int length() {
        return literal.getLiteral().length();
    }

    public boolean isLexicalFormValid() {
        if(literal.isRDFPlainLiteral()) {
            return true;
        }
        OWLDatatype datatype = literal.getDatatype();
        if(datatype.isBuiltIn()) {
            OWL2Datatype dt = OWL2Datatype.getDatatype(datatype.getIRI());
            return dt.getPattern().matcher(literal.getLiteral()).matches();
        }
        else {
            return true;
        }
    }


    public boolean isString() {
        return literal.getDatatype().isString();
    }

    public boolean isNumeric() {
        return numericTypes.contains(literal.getDatatype().getIRI());
    }
}
