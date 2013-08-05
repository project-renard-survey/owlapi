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

import javax.annotation.Nonnull;

/** Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group Date: 24-Oct-2006
 * <p/>
 * Represents an <a href=
 * "http://www.w3.org/TR/2009/REC-owl2-syntax-20091027/#Positive_Object_Property_Assertions"
 * >ObjectPropertyAssertion</a> axiom in the OWL 2 Specification. */
public interface OWLObjectPropertyAssertionAxiom extends
        OWLPropertyAssertionAxiom<OWLObjectPropertyExpression, OWLIndividual>,
        OWLSubClassOfAxiomShortCut {
    @Override
    @Nonnull
    OWLObjectPropertyAssertionAxiom getAxiomWithoutAnnotations();

    /** Gets a simplified version of this object property axiom. This is defined
     * recursively as follows:
     * <ul>
     * <li>ObjectPropertyAssertion(P S O) = ObjectPropertyAssertion(P S O)
     * <li>ObjectPropertyAssertion(ObjectInverseOf(P) S O) =
     * ObjectPropertyAssertion(P O S)
     * </ul>
     * 
     * @return the simplified version */
    @Nonnull
    OWLObjectPropertyAssertionAxiom getSimplified();

    /** Determines if this axiom is in a simplified form, i.e. a form where the
     * property is not a property inverse. ObjectPropertyAssertion(P S O) is in
     * a simplified form, where as ObjectPropertyAssertion(ObjectInverseOf(P) S
     * O) is not because it contains an inverse object property.
     * 
     * @return <code>true</code> if this axiom is in a simplified form,
     *         otherwise <code>false</code> */
    boolean isInSimplifiedForm();
}
