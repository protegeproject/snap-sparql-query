package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public abstract class Variable implements Atomic, HasIdentifier, HasVariables, HasName, AnnotationSubject {


    private final VariableNamePrefix prefix;

    private final String variableName;

    public Variable(String variableName) {
        checkNotNull(variableName);
        if(variableName.startsWith(VariableNamePrefix.QUESTION_MARK.getPrefix())) {
            prefix = VariableNamePrefix.QUESTION_MARK;
            this.variableName = variableName.substring(1);
        }
        else if(variableName.startsWith(VariableNamePrefix.DOLLAR.getPrefix())) {
            prefix = VariableNamePrefix.DOLLAR;
            this.variableName = variableName.substring(1);
        }
        else {
            prefix = VariableNamePrefix.getDefault();
            this.variableName = variableName;
        }
    }

    public Set<Variable> getVariables() {
        return Collections.<Variable>singleton(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Expression(");
        sb.append(getClass().getSimpleName());
        sb.append(" ?");
        sb.append(variableName);
        sb.append(")");
        return sb.toString();
    }

    public String getIdentifier() {
        return variableName;
    }

    public String getName() {
        return variableName;
    }

    public String getDisplayName() {
        return "?" + variableName;
    }

    public VariableNamePrefix getVariableNamePrefix() {
        return prefix;
    }

    public abstract RDFTerm getBound(IRI iri);

    public abstract PrimitiveType getType();

    @Override
    final public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Variable)) {
            return false;
        }
        Variable other = (Variable) obj;
        return this.variableName.equals(other.variableName);
    }

    @Override
    final public int hashCode() {
        return Objects.hashCode(getName());
    }

    public static Variable create(String name, PrimitiveType entityType) {
        if(entityType == PrimitiveType.CLASS) {
            return new ClassVariable(name);
        }
        else if(entityType == PrimitiveType.DATATYPE) {
            return new DatatypeVariable(name);
        }
        else if(entityType == PrimitiveType.OBJECT_PROPERTY) {
            return new ObjectPropertyVariable(name);
        }
        else if(entityType == PrimitiveType.DATA_PROPERTY) {
            return new DataPropertyVariable(name);
        }
        else if(entityType == PrimitiveType.ANNOTATION_PROPERTY) {
            return new AnnotationPropertyVariable(name);
        }
        else if(entityType == PrimitiveType.NAMED_INDIVIDUAL) {
            return new IndividualVariable(name);
        }
        else if(entityType == PrimitiveType.LITERAL) {
            return new LiteralVariable(name);
        }
        throw new IllegalStateException("Unknown EntityType: " + entityType);
    }
}
