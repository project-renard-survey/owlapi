/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.semanticweb.owlapi.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.semanticweb.owlapi.io.XMLUtils;
import org.semanticweb.owlapi.util.WeakCache;
import org.semanticweb.owlapi.vocab.Namespaces;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

/** Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Information Management Group<br>
 * Date: 14-Jan-2009 Represents International Resource Identifiers */
public class IRI implements OWLAnnotationSubject, OWLAnnotationValue, SWRLPredicate,
        CharSequence {
    /** Obtains this IRI as a URI. Note that Java URIs handle unicode characters,
     * so there is no loss during this translation.
     * 
     * @return The URI */
    @Nonnull
    public URI toURI() {
        if (remainder != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix);
            sb.append(remainder);
            return URI.create(sb.toString());
        } else {
            return URI.create(prefix);
        }
    }

    /** Determines if this IRI is absolute
     * 
     * @return <code>true</code> if this IRI is absolute or <code>false</code>
     *         if this IRI is not absolute */
    public boolean isAbsolute() {
        int colonIndex = prefix.indexOf(':');
        if (colonIndex == -1) {
            return false;
        }
        for (int i = 0; i < colonIndex; i++) {
            char ch = prefix.charAt(i);
            if (!Character.isLetter(ch) && !Character.isDigit(ch) && ch != '.'
                    && ch != '+' && ch != '-') {
                return false;
            }
        }
        return true;
    }

    /** @return the IRI scheme, e.g., http, urn... can be null */
    @Nullable
    public String getScheme() {
        int colonIndex = prefix.indexOf(':');
        if (colonIndex == -1) {
            return null;
        }
        return prefix.substring(0, colonIndex);
    }

    /** @return the prefix. Can be null. */
    @Nullable
    public String getNamespace() {
        return prefix;
    }

    /** @param s
     *            the IRI stirng to be resolved
     * @return s resolved against this IRI (with the URI::resolve() method,
     *         unless this IRI is opaque) */
    @Nonnull
    public IRI resolve(@Nonnull String s) {
        // shortcut: checking absolute and opaque here saves the creation of an
        // extra URI object
        URI uri = URI.create(s);
        if (uri.isAbsolute() || uri.isOpaque()) {
            return IRI.create(uri);
        }
        return IRI.create(toURI().resolve(uri).toString());
    }

    /** Determines if this IRI is in the reserved vocabulary. An IRI is in the
     * reserved vocabulary if it starts with
     * &lt;http://www.w3.org/1999/02/22-rdf-syntax-ns#&gt; or
     * &lt;http://www.w3.org/2000/01/rdf-schema#&gt; or
     * &lt;http://www.w3.org/2001/XMLSchema#&gt; or
     * &lt;http://www.w3.org/2002/07/owl#&gt;
     * 
     * @return <code>true</code> if the IRI is in the reserved vocabulary,
     *         otherwise <code>false</code>. */
    public boolean isReservedVocabulary() {
        return prefix.startsWith(Namespaces.OWL.toString())
                || prefix.startsWith(Namespaces.RDF.toString())
                || prefix.startsWith(Namespaces.RDFS.toString())
                || prefix.startsWith(Namespaces.XSD.toString());
    }

    /** Determines if this IRI is equal to the IRI that <code>owl:Thing</code> is
     * named with
     * 
     * @return <code>true</code> if this IRI is equal to
     *         &lt;http://www.w3.org/2002/07/owl#Thing&gt; and otherwise
     *         <code>false</code> */
    public boolean isThing() {
        return remainder != null && remainder.equals("Thing")
                && prefix.equals(Namespaces.OWL.toString());
    }

    /** Determines if this IRI is equal to the IRI that <code>owl:Nothing</code>
     * is named with
     * 
     * @return <code>true</code> if this IRI is equal to
     *         &lt;http://www.w3.org/2002/07/owl#Nothing&gt; and otherwise
     *         <code>false</code> */
    public boolean isNothing() {
        return equals(OWLRDFVocabulary.OWL_NOTHING.getIRI());
    }

    /** Determines if this IRI is equal to the IRI that is named
     * <code>rdf:PlainLiteral</code>
     * 
     * @return <code>true</code> if this IRI is equal to
     *         &lt;http://www.w3.org/1999/02/22-rdf-syntax-ns#PlainLiteral&gt;,
     *         otherwise <code>false</code> */
    public boolean isPlainLiteral() {
        return remainder != null && remainder.equals("PlainLiteral")
                && prefix.equals(Namespaces.RDF.toString());
    }

    /** Gets the fragment of the IRI.
     * 
     * @return The IRI fragment, or <code>null</code> if the IRI does not have a
     *         fragment */
    @Nullable
    public String getFragment() {
        return remainder;
    }

    /** Obtained this IRI surrounded by angled brackets
     * 
     * @return This IRI surrounded by &lt; and &gt; */
    @Nonnull
    public String toQuotedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(prefix);
        if (remainder != null) {
            sb.append(remainder);
        }
        sb.append(">");
        return sb.toString();
    }

    /** Creates an IRI from the specified String.
     * 
     * @param str
     *            The String that specifies the IRI. Cannot be null.
     * @return The IRI that has the specified string representation. */
    @Nonnull
    public static IRI create(@Nonnull String str) {
        checkNotNull(str);
        return new IRI(str);
    }

    /** Creates an IRI by concatenating two strings. The full IRI is an IRI that
     * contains the characters in prefix + suffix.
     * 
     * @param prefix
     *            The first string. May be <code>null</code>.
     * @param suffix
     *            The second string. May be <code>null</code>.
     * @return An IRI whose characters consist of prefix + suffix.
     * @since 3.3 */
    @Nonnull
    public static IRI create(@Nullable String prefix, @Nullable String suffix) {
        if (prefix == null) {
            return new IRI(suffix);
        } else if (suffix == null) {
            // suffix set deliberately to null is used only in blank node
            // management
            // this is not great but blank nodes should be changed to not refer
            // to IRIs at all
            // XXX address blank node issues with iris
            return new IRI(prefix);
        } else {
            int index = XMLUtils.getNCNameSuffixIndex(prefix);
            int test = XMLUtils.getNCNameSuffixIndex(suffix);
            if (index == -1 && test == 0) {
                // the prefix does not contain an ncname character and there is
                // no illegal character in the suffix
                // the split is therefore correct
                return new IRI(prefix, suffix);
            }
            // otherwise the split is wrong; we could obtain the right split by
            // using index and test, but it's just as easy to use the other
            // constructor
            return new IRI(prefix + suffix);
        }
    }

    /** @param file
     *            the file to create the IRI from. Cannot be null.
     * @return file.toURI() IRI */
    @Nonnull
    public static IRI create(@Nonnull File file) {
        checkNotNull(file);
        return new IRI(file.toURI());
    }

    /** @param uri
     *            the uri to create the IRI from. Cannot be null
     * @return the IRI wrapping the uri */
    @Nonnull
    public static IRI create(@Nonnull URI uri) {
        checkNotNull(uri);
        return new IRI(uri);
    }

    /** @param url
     *            the url to create the IRI from. Cannot be null.
     * @return an IRI wraopping url.toURI() if the URL is ill formed */
    @Nonnull
    public static IRI create(@Nonnull URL url) {
        checkNotNull(url);
        try {
            return new IRI(url.toURI());
        } catch (URISyntaxException e) {
            throw new OWLRuntimeException(e);
        }
    }

    /** Gets an auto-generated ontology document IRI.
     * 
     * @return An auto-generated ontology document IRI. The IRI has the form
     *         <code>owlapi:ontologyNNNNNNNNNNN</code> */
    @Nonnull
    public static IRI generateDocumentIRI() {
        return create("owlapi:ontology" + counter.incrementAndGet());
    }

    private static final AtomicLong counter = new AtomicLong(System.nanoTime());
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // //
    // // Impl - All constructors are private - factory methods are used for
    // public creation
    // //
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = 40000L;
    private static WeakCache<String> prefixCache = new WeakCache<String>();
    private final String remainder;
    private final String prefix;
    private int hashCode = 0;

    /** Constructs an IRI which is built from the concatenation of the specified
     * prefix and suffix.
     * 
     * @param prefix
     *            The prefix.
     * @param fragment
     *            The suffix. */
    protected IRI(@Nonnull String prefix, @Nullable String fragment) {
        this.prefix = prefixCache.cache(prefix);
        remainder = fragment;
    }

    protected IRI(@Nonnull String s) {
        this(XMLUtils.getNCNamePrefix(s), XMLUtils.getNCNameSuffix(s));
    }

    protected IRI(@Nonnull URI uri) {
        this(checkNotNull(uri).toString());
    }

    @Override
    public int length() {
        return prefix.length() + (remainder == null ? 0 : remainder.length());
    }

    @Override
    public char charAt(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(Integer.toString(index));
        }
        if (index >= length()) {
            throw new IndexOutOfBoundsException(Integer.toString(index));
        }
        if (index < prefix.length()) {
            return prefix.charAt(index);
        }
        return remainder.charAt(index - prefix.length());
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(remainder);
        return sb.subSequence(start, end);
    }

    @Override
    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(OWLAnnotationSubjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <E> E accept(OWLAnnotationSubjectVisitorEx<E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Set<OWLClass> getClassesInSignature() {
        return Collections.emptySet();
    }

    @Override
    public Set<OWLDataProperty> getDataPropertiesInSignature() {
        return Collections.emptySet();
    }

    @Override
    public Set<OWLNamedIndividual> getIndividualsInSignature() {
        return Collections.emptySet();
    }

    @Override
    public Set<OWLObjectProperty> getObjectPropertiesInSignature() {
        return Collections.emptySet();
    }

    @Override
    public Set<OWLEntity> getSignature() {
        return Collections.emptySet();
    }

    @Override
    public Set<OWLAnonymousIndividual> getAnonymousIndividuals() {
        return Collections.emptySet();
    }

    @Override
    public Set<OWLDatatype> getDatatypesInSignature() {
        return Collections.emptySet();
    }

    @Override
    public Set<OWLClassExpression> getNestedClassExpressions() {
        return Collections.emptySet();
    }

    @Override
    public int compareTo(OWLObject o) {
        if (o == this) {
            return 0;
        }
        if (!(o instanceof IRI)) {
            return -1;
        }
        IRI other = (IRI) o;
        int diff = prefix.compareTo(other.prefix);
        if (diff != 0) {
            return diff;
        }
        String otherRemainder = other.remainder;
        if (remainder == null) {
            if (otherRemainder == null) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (otherRemainder == null) {
                return 1;
            } else {
                return remainder.compareTo(otherRemainder);
            }
        }
    }

    @Override
    public String toString() {
        if (remainder != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix);
            sb.append(remainder);
            return sb.toString();
        } else {
            return prefix;
        }
    }

    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = prefix.hashCode() + (remainder != null ? remainder.hashCode() : 0);
        }
        return hashCode;
    }

    @Override
    public void accept(OWLAnnotationValueVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <O> O accept(OWLAnnotationValueVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isTopEntity() {
        return false;
    }

    @Override
    public boolean isBottomEntity() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IRI)) {
            return false;
        }
        IRI other = (IRI) obj;
        String otherRemainder = other.remainder;
        if (remainder == null) {
            return otherRemainder == null && prefix.equals(other.prefix);
        } else {
            return otherRemainder != null && remainder.equals(otherRemainder)
                    && other.prefix.equals(prefix);
        }
    }
}
