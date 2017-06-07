/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.modelneo4j.genericapproach;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.iobserve.analysis.modelneo4j.PcmRelationshipType;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;

/**
 *
 * @author Lars Bluemke
 *
 * @param <T>
 */
public class GenericComponentProvider<T extends EObject> {

    public static final String ID = "id";
    public static final String ENTITY_NAME = "entityName";
    public static final String TYPE = "type";
    public static final String REF_NAME = "refName";

    private final GraphDatabaseService graph;
    private final HashMap<String, DataType> dataTypes;

    public GenericComponentProvider(final GraphDatabaseService graph) {
        this.graph = graph;
        this.dataTypes = new HashMap<>();
    }

    public GenericComponentProvider(final GraphDatabaseService graph, final HashMap<String, DataType> dataTypes) {
        this.graph = graph;
        this.dataTypes = dataTypes;
    }

    public Node createComponent(final T component) {
        /** Create a label representing the type of the component */
        final Label label = Label.label(ModelNeo4jUtil.getTypeName(component.eClass()));
        // System.out.println("Writing " + this.getTypeName(component.eClass()));
        Node node = null;

        /**
         * For Entities and DataTypes: Look if there already is a node for the component in the
         * graph. Note that complex data types are entities. For everything else: Always create a
         * new node.
         */
        if (component instanceof Entity) {
            try (Transaction tx = this.getGraph().beginTx()) {
                node = this.getGraph().findNode(label, GenericComponentProvider.ID, ((Entity) component).getId());
                tx.success();
            }
        } else if (component instanceof PrimitiveDataType) {
            try (Transaction tx = this.getGraph().beginTx()) {
                node = this.getGraph().findNode(label, GenericComponentProvider.TYPE,
                        ((PrimitiveDataType) component).getType().name());
                tx.success();
            }
        }

        /** If there is no node yet, create one */
        if (node == null) {

            try (Transaction tx = this.getGraph().beginTx()) {
                node = this.getGraph().createNode(label);

                // System.out.println("writing " + component + " " + label.name());
                /** Iterate over all attributes */
                for (final EAttribute attr : component.eClass().getEAllAttributes()) {
                    final Object value = component.eGet(attr);
                    if (value != null) {
                        node.setProperty(attr.getName(), value.toString());
                        // System.out.println("\t" + component + " attribute " + attr.getName() + "
                        // " + value.toString());
                    }
                }

                tx.success();
            }

            /** Iterate over all references */
            for (final EReference ref : component.eClass().getEAllReferences()) {

                /**
                 * Save references of the component as references to other nodes in the graph
                 */
                final Object refReprensation = component.eGet(ref);
                // System.out.println("\t" + component + " all refs " + ref + " " +
                // refReprensation);

                /**
                 * If references reference multiple other components they are represented as a list
                 * otherwise they are not represented as a list
                 *
                 * TODO: Make sure the order of the list is kept!
                 */
                if (refReprensation instanceof EList<?>) {

                    /** Store each single reference */
                    for (final Object o : (EList<?>) component.eGet(ref)) {
                        // System.out.println("\t" + component + " reference " + o);

                        /** Let a new provider create a node for the referenced component */
                        final Node refNode = new GenericComponentProvider<>(this.graph).createComponent((EObject) o);

                        /** When the new node is created, create a reference */
                        try (Transaction tx = this.getGraph().beginTx()) {
                            final Relationship rel = node.createRelationshipTo(refNode,
                                    ModelNeo4jUtil.getRelationshipType(ref, o));
                            rel.setProperty(GenericComponentProvider.REF_NAME, ref.getName());
                            tx.success();
                        }
                    }
                } else {
                    if (refReprensation != null) {
                        // System.out.println("\t" + component + " reference " + refReprensation);

                        /** Let a new provider create a node for the referenced component */
                        final Node refNode = new GenericComponentProvider<>(this.graph)
                                .createComponent((EObject) refReprensation);

                        /** When the new node is created, create a reference */
                        try (Transaction tx = this.getGraph().beginTx()) {
                            final Relationship rel = node.createRelationshipTo(refNode,
                                    ModelNeo4jUtil.getRelationshipType(ref, refReprensation));
                            rel.setProperty(GenericComponentProvider.REF_NAME, ref.getName());
                            tx.success();
                        }
                    }
                }
            }
        }

        return node;
    }

    public EObject readComponent(final String typeName, final String id) {
        final Label label = Label.label(typeName);
        Node node;

        try (Transaction tx = this.getGraph().beginTx()) {
            node = this.getGraph().findNode(label, GenericComponentProvider.ID, id);
            tx.success();
        }

        return this.readComponent(node);
    }

    public EObject readComponent(final Node node) {
        /**
         * Get the node's data type from its label and instantiate a new empty object of this data
         * type
         */
        Label label;
        try (Transaction tx = this.getGraph().beginTx()) {
            label = ModelNeo4jUtil.getFirstLabel(node.getLabels());
            tx.success();
        }

        final EObject component = ModelNeo4jUtil.instantiateEObject(label.name());

        /** Iterate over all attributes */
        try (Transaction tx = this.getGraph().beginTx()) {

            // System.out.println("reading " + component + " " + label.name());
            for (final EAttribute attr : component.eClass().getEAllAttributes()) {
                // System.out.print("\t" + component + " attribute " + attr.getName() + " = ");
                try {
                    final Object value = ModelNeo4jUtil.instantiateAttribute(
                            attr.getEAttributeType().getInstanceClass(), node.getProperty(attr.getName()).toString());
                    // System.out.println(value);
                    if (value != null) {
                        component.eSet(attr, value);
                    }
                } catch (final NotFoundException e) {
                    component.eSet(attr, null);
                }
            }
            tx.success();
        }

        /** Iterate over all references */
        for (final EReference ref : component.eClass().getEAllReferences()) {
            final String refName = ref.getName();
            Object refReprensation = component.eGet(ref);

            try (final Transaction tx = this.getGraph().beginTx()) {
                /** Iterate over all outgoing containment relationships of the node */
                for (final Relationship rel : node.getRelationships(Direction.OUTGOING)) {
                    /** If a relationship in the graph matches the references name... */
                    if (refName.equals(rel.getProperty(GenericComponentProvider.REF_NAME))) {
                        final Node endNode = rel.getEndNode();

                        /** Only create a new object for containments */
                        if (rel.isType(PcmRelationshipType.CONTAINS)) {
                            // System.out.println("\t" + component + " reference " + refName);
                            /** ...recursively create an instance of the referenced object */
                            if (refReprensation instanceof EList<?>) {
                                final EObject endComponent = new GenericComponentProvider<>(this.graph, this.dataTypes)
                                        .readComponent(endNode);
                                ((EList<EObject>) refReprensation).add(endComponent);
                            } else {
                                refReprensation = new GenericComponentProvider<>(this.graph, this.dataTypes)
                                        .readComponent(endNode);
                                component.eSet(ref, refReprensation);

                            }
                        } else if (rel.isType(PcmRelationshipType.IS_TYPE)) {
                            // System.out.println("\t" + component + " reference " + refName);
                            if (refReprensation instanceof EList<?>) {
                                final EObject endComponent = new DataTypeProvider(this.graph, this.dataTypes)
                                        .readComponent(endNode);
                                ((EList<EObject>) refReprensation).add(endComponent);
                            } else {
                                refReprensation = new DataTypeProvider(this.graph, this.dataTypes)
                                        .readComponent(endNode);
                                component.eSet(ref, refReprensation);
                                // System.out.println(">>>>>>Datatype2 " + refReprensation);
                            }
                        }
                    }
                }
                tx.success();
            }
        }

        return component;
    }

    public List<String> readComponent(final String typeName) {
        try (Transaction tx = this.getGraph().beginTx()) {
            final ResourceIterator<Node> nodes = this.graph.findNodes(Label.label(typeName));
            final LinkedList<String> ids = new LinkedList<>();

            while (nodes.hasNext()) {
                final Node n = nodes.next();
                ids.add(n.getProperty(GenericComponentProvider.ID).toString());
            }

            tx.success();
            return ids;
        }
    }

    public void updateComponent(final T component) {
    }

    public void deleteComponent(final T component) {
    }

    public HashMap<String, DataType> getDataTypes() {
        return this.dataTypes;
    }

    public GraphDatabaseService getGraph() {
        return this.graph;
    }
}
