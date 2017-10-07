package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import java.util.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 26/07/2012
 */
public class Datatype extends AbstractEntity implements AtomicDatatype {

    private static final ImmutableMap<IRI, Datatype> iri2Datatype;

    static {
        ImmutableMap.Builder<IRI, Datatype> iriMapBuilder = ImmutableMap.builder();
        for (OWL2Datatype owl2Datatype : OWL2Datatype.values()) {
            Datatype datatype = new Datatype(owl2Datatype);
            iriMapBuilder.put(owl2Datatype.getIRI(), datatype);
        }




        iri2Datatype = iriMapBuilder.build();
    }


    private static final Datatype RDF_PLAIN_LITERAL = new Datatype(OWLRDFVocabulary.RDF_PLAIN_LITERAL.getIRI());

    private static final Datatype XSD_STRING = new Datatype(OWL2Datatype.XSD_STRING);



    private static final Datatype XSD_BOOLEAN = new Datatype(OWL2Datatype.XSD_BOOLEAN);

    private static final Datatype XSD_DOUBLE = new Datatype(OWL2Datatype.XSD_DOUBLE);

    private static final Datatype XSD_FLOAT = new Datatype(OWL2Datatype.XSD_FLOAT);

    private static final Datatype XSD_DATETIME = new Datatype(OWL2Datatype.XSD_DATE_TIME);



    private static final Datatype XSD_DECIMAL = new Datatype(OWL2Datatype.XSD_DECIMAL);

    private static final Datatype XSD_INTEGER = new Datatype(OWL2Datatype.XSD_INTEGER);

    private static final Datatype XSD_NON_POSITIVE_INTEGER = new Datatype(OWL2Datatype.XSD_NON_POSITIVE_INTEGER);

    private static final Datatype XSD_NON_NEGATIVE_INTEGER = new Datatype(OWL2Datatype.XSD_NON_NEGATIVE_INTEGER);

    private static final Datatype XSD_LONG = new Datatype(OWL2Datatype.XSD_LONG);

    private static final Datatype XSD_INT = new Datatype(OWL2Datatype.XSD_INT);

    private static final Datatype XSD_SHORT = new Datatype(OWL2Datatype.XSD_SHORT);

    private static final Datatype XSD_BYTE = new Datatype(OWL2Datatype.XSD_BYTE);

    private static final Datatype XSD_NEGATIVE_INTEGER = new Datatype(OWL2Datatype.XSD_NEGATIVE_INTEGER);

    private static final Datatype XSD_UNSIGNED_LONG = new Datatype(OWL2Datatype.XSD_UNSIGNED_LONG);

    private static final Datatype XSD_UNSIGNED_INT = new Datatype(OWL2Datatype.XSD_UNSIGNED_INT);

    private static final Datatype XSD_UNSIGNED_SHORT = new Datatype(OWL2Datatype.XSD_UNSIGNED_SHORT);

    private static final Datatype XSD_UNSIGNED_BYTE = new Datatype(OWL2Datatype.XSD_UNSIGNED_BYTE);

    private static final Datatype XSD_POSITIVE_INTEGER = new Datatype(OWL2Datatype.XSD_POSITIVE_INTEGER);


    private static final ImmutableSet<Datatype> NUMERIC_TYPES;

    private static final ImmutableSet<Datatype> XSD_INTEGER_DERIVED_TYPES;

    static {
        //@formatter:off
        NUMERIC_TYPES = ImmutableSet.<Datatype>builder()
                .add(XSD_DOUBLE)
                .add(XSD_FLOAT)
                .add(XSD_DECIMAL)
                .add(XSD_INTEGER)
                .add(XSD_NON_POSITIVE_INTEGER)
                .add(XSD_NON_NEGATIVE_INTEGER)
                .add(XSD_LONG)
                .add(XSD_INT)
                .add(XSD_SHORT)
                .add(XSD_BYTE)
                .add(XSD_NEGATIVE_INTEGER)
                .add(XSD_UNSIGNED_LONG)
                .add(XSD_UNSIGNED_INT)
                .add(XSD_UNSIGNED_SHORT)
                .add(XSD_UNSIGNED_BYTE)
                .add(XSD_POSITIVE_INTEGER)
                .build();

        XSD_INTEGER_DERIVED_TYPES = ImmutableSet.<Datatype>builder()
                .add(XSD_INTEGER)
                .add(XSD_NON_POSITIVE_INTEGER)
                .add(XSD_NEGATIVE_INTEGER)
                .add(XSD_NON_NEGATIVE_INTEGER)
                .add(XSD_POSITIVE_INTEGER)
                .add(XSD_LONG)
                .add(XSD_UNSIGNED_LONG)
                .add(XSD_INT)
                .add(XSD_UNSIGNED_INT)
                .add(XSD_SHORT)
                .add(XSD_UNSIGNED_SHORT)
                .add(XSD_BYTE)
                .add(XSD_UNSIGNED_BYTE)
                .build();
        
        //@formatter:on
    }


    private final Optional<OWL2Datatype> owl2Datatype;

    public Datatype(@Nonnull IRI iri) {
        super(iri);
        owl2Datatype = Optional.empty();
    }

    private Datatype(OWL2Datatype datatype) {
        super(datatype.getIRI());
        this.owl2Datatype = Optional.of(datatype);
    }


    public static Datatype get(IRI iri) {
        Datatype result = iri2Datatype.get(iri);
        if (result != null) {
            return result;
        }
        return new Datatype(iri);
    }

    public static Datatype getRDFPlainLiteral() {
        return RDF_PLAIN_LITERAL;
    }

    public static Datatype getXSDString() {
        return XSD_STRING;
    }

    public static Datatype getXSDBoolean() {
        return XSD_BOOLEAN;
    }

    public static Datatype getXSDInteger() {
        return XSD_INTEGER;
    }

    public static Datatype getXSDDouble() {
        return XSD_DOUBLE;
    }

    public static Datatype getXSDFloat() {
        return XSD_FLOAT;
    }

    public static Datatype getXSDDecimal() {
        return XSD_DECIMAL;
    }

    public static Datatype getXSDLong() {
        return XSD_LONG;
    }

    public static Datatype getXSDDateTime() {
        return XSD_DATETIME;
    }

    public boolean isInLexicalSpace(String lexicalValue) {
        return !owl2Datatype.isPresent() || owl2Datatype.get().getPattern().matcher(lexicalValue).matches();
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Datatype(" + getIRI() + ")";
    }

    public boolean isRDFPlainLiteral() {
        return RDF_PLAIN_LITERAL.equals(this);
    }

    public boolean isXSDString() {
        return XSD_STRING.equals(this);
    }

    public boolean isXSDBoolean() {
        return XSD_BOOLEAN.equals(this);
    }

    public boolean isXSDDecimal() {
        return XSD_DECIMAL.equals(this);
    }

    public boolean isXSDDecimalDerived() {
        return this.isXSDDecimal() || XSD_INTEGER_DERIVED_TYPES.contains(this);
    }

    public boolean isXSDIntegerDerived() {
        return isXSDInteger() || XSD_INTEGER_DERIVED_TYPES.contains(this);
    }

    public boolean isXSDFloat() {
        return XSD_FLOAT.equals(this);
    }
    public boolean isXSDInteger() {
        return XSD_INTEGER.equals(this);
    }

    public boolean isXSDDouble() {
        return XSD_DOUBLE.equals(this);
    }

    public boolean isNumeric() {
        return !isXSDString() && !isRDFPlainLiteral() && NUMERIC_TYPES.contains(this);
    }

    public boolean isXSDDateTime() {
        return XSD_DATETIME.equals(this);
    }

    @Override
    public int hashCode() {
        return getIRI().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Datatype)) {
            return false;
        }
        Datatype other = (Datatype) obj;
        return this.getIRI().equals(other.getIRI());
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }

    @Override
    public Optional<Datatype> bind(SolutionMapping sm) {
        return Optional.of(this);
    }

    @Override
    public OWLDatatype toOWLObject(OWLDataFactory df) {
        return df.getOWLDatatype(getIRI());
    }
}
