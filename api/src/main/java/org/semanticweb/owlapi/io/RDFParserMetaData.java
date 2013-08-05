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
package org.semanticweb.owlapi.io;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.util.CollectionFactory;

/** Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 21/12/2010
 * 
 * @since 3.2 */
public class RDFParserMetaData implements OWLOntologyLoaderMetaData {
    private final int tripleCount;
    private final RDFOntologyHeaderStatus headerStatus;
    private final Set<RDFTriple> unparsedTriples = new HashSet<RDFTriple>();

    /** @param headerStatus
     *            the header status
     * @param tripleCount
     *            the triple count
     * @param unparsedTriples
     *            the set of triples not parsed */
    public RDFParserMetaData(@Nonnull RDFOntologyHeaderStatus headerStatus,
            int tripleCount, @Nonnull Set<RDFTriple> unparsedTriples) {
        this.tripleCount = tripleCount;
        this.headerStatus = checkNotNull(headerStatus);
        this.unparsedTriples.addAll(checkNotNull(unparsedTriples));
    }

    /** Gets a count of the triples process during loading.
     * 
     * @return The number of triples process during loading. */
    public int getTripleCount() {
        return tripleCount;
    }

    /** @return the header status */
    public RDFOntologyHeaderStatus getHeaderState() {
        return headerStatus;
    }

    /** @return the set of unparsed triples, as a copy */
    public Set<RDFTriple> getUnparsedTriples() {
        return CollectionFactory
                .getCopyOnRequestSetFromMutableCollection(unparsedTriples);
    }
}
