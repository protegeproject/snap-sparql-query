package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 6 Oct 2017
 */
public class SPARQLPrefixManager {

    private final static Comparator<String> LONGEST_FIRST = (o1, o2) -> {
        int diff = -(o1.length() - o2.length());
        if(diff != 0) {
            return diff;
        }
        return o1.compareTo(o2);
    };


    private final static Comparator<String> c = Comparator
            .comparing(String::length)
            .reversed()
            .thenComparing(String::compareTo);

    private final Map<String, String> prefixName2PrefixMap = new HashMap<>();

    private final Map<String, String> prefix2PrefixNameMap = new TreeMap<>(c);

    /**
     * Sets the mapping from the specified prefix name to the specified prefix.
     * @param prefixName The prefix name.  This must be a valid prefix name i.e. it must end with a colon.
     * @param prefix The prefix.
     * @throws IllegalArgumentException if the specified prefix name does not end with a colon.
     */
    public void setPrefix(@Nonnull String prefixName,
                          @Nonnull String prefix) {
        if(!checkNotNull(prefixName).endsWith(":")) {
            throw new IllegalArgumentException("Specified prefix name " + prefixName + " does not end with a colon");
        }
        prefixName2PrefixMap.put(prefixName, checkNotNull(prefix));
        prefix2PrefixNameMap.put(checkNotNull(prefix), prefixName);
    }

    /**
     * Determines if the specified prefix name is mapped to a prefix.
     * @param prefixName The prefix name. This should end with a colon.
     * @return true if the specified prefix name maps to a prefix otherwise false.
     */
    public boolean containsPrefixMapping(@Nonnull String prefixName) {
        return prefixName2PrefixMap.containsKey(prefixName);
    }

    /**
     * Turns the specified prefixed name into an IRI.
     * @param prefixedName The prefixed name.  This prefix name MUST have a mapping in this manager
     * @return The expanded prefixed name as an IRI
     */
    @Nonnull
    public IRI getIRI(@Nonnull String prefixedName) {
        int prefixNameColonIndex = checkNotNull(prefixedName).indexOf(":");
        if(prefixNameColonIndex == -1) {
            return IRI.create(prefixedName);
        }
        String prefixName = prefixedName.substring(0, prefixNameColonIndex + 1);
        String prefix = prefixName2PrefixMap.get(prefixName);
        if(prefix == null) {
            throw new IllegalArgumentException("The prefix name (" + prefixedName + ") is not mapped to a prefix by this manager.");
        }
        String localName = prefixedName.substring(prefixNameColonIndex + 1);
        return IRI.create(prefix + localName);
    }

    /**
     * Gets the IRI as a prefixed name if a prefix name mapping exists.
     * @param iri The IRI.
     * @return The prefixed name form of the IRI, or the IRI as a quoted IRI string if there is no mapping.
     */
    @Nonnull
    public String getPrefixedNameOrIri(@Nonnull IRI iri) {
        String iriString = iri.toString();
        return prefix2PrefixNameMap.entrySet().stream()
                .filter(e -> iriString.startsWith(e.getKey()))
                .map(e -> e.getValue() + iriString.substring(e.getKey().length()))
                .findFirst()
                .orElse(iri.toQuotedString());
    }

    public Map<String, String> asMap() {
        return new HashMap<>(prefixName2PrefixMap);
    }
}
