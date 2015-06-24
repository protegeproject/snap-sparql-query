package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 23/06/15
 */
public class GroupKey {

    private final ImmutableList<EvaluationResult> key;

    private final static GroupKey EMPTY = new GroupKey(ImmutableList.of());

    public GroupKey(ImmutableList<EvaluationResult> key) {
        this.key = key;
    }

    public static GroupKey empty() {
        return EMPTY;
    }

    public ImmutableList<EvaluationResult> getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GroupKey)) {
            return false;
        }
        GroupKey other = (GroupKey) obj;
        return this.key.equals(other.key);
    }


    @Override
    public String toString() {
        return toStringHelper("GroupKey")
                .addValue(key)
                .toString();
    }
}
