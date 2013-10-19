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
package org.coode.owlapi.manchesterowlsyntax;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

/** Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Information Management Group<br>
 * Date: 18-Feb-2009 */
public class OntologyAxiomPair {
    private OWLOntology ontology;
    private OWLAxiom axiom;

    /** @param ontology
     * @param axiom */
    public OntologyAxiomPair(OWLOntology ontology, OWLAxiom axiom) {
        this.ontology = ontology;
        this.axiom = axiom;
    }

    /** @return the ontology */
    public OWLOntology getOntology() {
        return ontology;
    }

    /** @return the axiom */
    public OWLAxiom getAxiom() {
        return axiom;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof OntologyAxiomPair)) {
            return false;
        }
        OntologyAxiomPair other = (OntologyAxiomPair) obj;
        if (ontology != null && other.ontology != null) {
            return ontology.equals(other.ontology) && axiom.equals(other.axiom);
        } else if (ontology != null && other.ontology == null) {
            return false;
        } else if (ontology == null && other.ontology != null) {
            return false;
        } else {
            return axiom.equals(other.axiom);
        }
    }

    @Override
    public int hashCode() {
        if (ontology != null) {
            return ontology.hashCode() + axiom.hashCode();
        } else {
            return 37 + axiom.hashCode();
        }
    }

    @Override
    public String toString() {
        return axiom.toString() + " in " + (ontology != null ? ontology.toString() : "");
    }
}
