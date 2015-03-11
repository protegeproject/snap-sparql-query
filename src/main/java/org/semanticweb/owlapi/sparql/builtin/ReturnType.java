package org.semanticweb.owlapi.sparql.builtin;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class ReturnType {

    private SparqlType sparqlType;

    public ReturnType(SparqlType sparqlType) {
        this.sparqlType = sparqlType;
    }

    public SparqlType getSparqlType() {
        return sparqlType;
    }

    public boolean isSimpleLiteral() {
        return sparqlType == SparqlType.SIMPLE_LITERAL;
    }

    public boolean isBoolean() {
        return sparqlType == SparqlType.BOOLEAN;
    }

    public boolean isStringLiteral() {
        return sparqlType == SparqlType.STRING_LITERAL;
    }

    public boolean isNumeric() {
        return sparqlType == SparqlType.NUMERIC || sparqlType == SparqlType.XSD_DOUBLE || sparqlType == SparqlType.XSD_STRING;
    }

    public boolean isLiteral() {
        return sparqlType == SparqlType.LITERAL
                || sparqlType == SparqlType.SIMPLE_LITERAL
                || sparqlType == SparqlType.STRING_LITERAL;
    }

    public boolean isIRI() {
        return sparqlType == SparqlType.IRI;
    }
}
