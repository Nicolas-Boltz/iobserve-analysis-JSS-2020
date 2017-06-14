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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;

/**
 *
 * @author Lars Bluemke
 *
 * @param <T>
 */
public class ModelProvider<T extends EObject> {

    private static final String ID = "id";
    private static final String TYPE = "type";
    private static final String REF_NAME = "refName";

    private static final String ACCESSIBLE = "accessible";
    private static final String DELETE = "delete";
    private static final String VISITED = "visited";

    private final GraphDatabaseService graph;

    public ModelProvider(final GraphDatabaseService graph) {
        this.graph = graph;
    }

    /**
     * Writes the given component into the provider's {@link #graph}.
     *
     * @param component
     *            Component to save
     * @return Root node of the component's graph
     */
    public Node createComponent(final T component) {
        final Node node;
        try (Transaction tx = this.getGraph().beginTx()) {
            node = this.createNodes(component);
            tx.success();
        }
        return node;
    }

    /**
     * Helper method for writing: Writes the given component into the provider's {@link #graph}
     * recursively. Calls to this method have to be performed from inside a {@link Transaction}.
     *
     * @param component
     *            Component to save
     * @return Root node of the component's graph
     */
    private Node createNodes(final EObject component) {
        // Create a label representing the type of the component
        final Label label = Label.label(ModelProviderUtil.getTypeName(component.eClass()));
        Node node = null;

        // For components with an unique id and primitive data types: Look if there already is a
        // node for the component in the graph. For everything else: Always create a new node.
        final EAttribute idAttr = component.eClass().getEIDAttribute();
        if (idAttr != null) {
            node = this.getGraph().findNode(label, ModelProvider.ID, component.eGet(idAttr));
        } else if (component instanceof PrimitiveDataType) {
            node = this.getGraph().findNode(label, ModelProvider.TYPE,
                    ((PrimitiveDataType) component).getType().name());
        }

        // If there is no node yet, create one
        if (node == null) {

            node = this.getGraph().createNode(label);

            // System.out.println("writing " + component + " " + label.name());

            // Save attributes as node properties
            for (final EAttribute attr : component.eClass().getEAllAttributes()) {
                final Object value = component.eGet(attr);
                if (value != null) {
                    node.setProperty(attr.getName(), value.toString());
                    // System.out.println("\t" + component + " attribute " + attr.getName() + "
                    // " + value.toString());
                }
            }

            // Save references as relations between nodes
            for (final EReference ref : component.eClass().getEAllReferences()) {

                final Object refReprensation = component.eGet(ref);
                // System.out.println("\t" + component + " all refs " + ref + " " +
                // refReprensation);

                // 0..* refs are represented as a list and 1 refs are represented directly
                if (refReprensation instanceof EList<?>) {

                    for (final Object o : (EList<?>) component.eGet(ref)) {
                        // System.out.println("\t" + component + " reference " + o);

                        final Node refNode = this.createNodes((EObject) o);

                        final Relationship rel = node.createRelationshipTo(refNode,
                                ModelProviderUtil.getRelationshipType(ref, o));
                        rel.setProperty(ModelProvider.REF_NAME, ref.getName());
                    }
                } else {
                    if (refReprensation != null) {
                        // System.out.println("\t" + component + " reference " +
                        // refReprensation);

                        final Node refNode = this.createNodes((EObject) refReprensation);

                        final Relationship rel = node.createRelationshipTo(refNode,
                                ModelProviderUtil.getRelationshipType(ref, refReprensation));
                        rel.setProperty(ModelProvider.REF_NAME, ref.getName());
                    }
                }
            }
        }

        return node;
    }

    /**
     * Reads a specified component from the provider's {@link #graph}.
     *
     * @param clazz
     *            Data type of component to be read
     * @param id
     *            Id of component to be read
     * @return The read component
     */
    public EObject readComponent(final Class<T> clazz, final String id) {
        final Label label = Label.label(clazz.getSimpleName());
        Node node;
        EObject component;

        try (Transaction tx = this.getGraph().beginTx()) {
            node = this.getGraph().findNode(label, ModelProvider.ID, id);
            component = this.readComponent(node, new HashMap<Long, DataType>());
            tx.success();
        }

        return component;
    }

    /**
     * Helper method for reading: Starting from a given node this method recursively reads all
     * contained successor nodes and instantiates the correspondent ecore objects. To make sure that
     * data types are instantiated just once a map from node ids to ecore objects is passed for data
     * types. Calls to this method have to be performed from inside a {@link Transaction}.
     *
     * @param node
     *            The node to start with
     * @param dataTypes
     *            Map of node ids to already instantiated ecore objects
     * @return The root's ecore object
     */
    private EObject readComponent(final Node node, final HashMap<Long, DataType> dataTypes) {
        // Get node's data type label and instantiate a new empty object of this data type
        final Label label = ModelProviderUtil.getFirstLabel(node.getLabels());
        final EObject component = ModelProviderUtil.instantiateEObject(label.name());

        // Load attribute values from the node
        final Iterator<Map.Entry<String, Object>> i = node.getAllProperties().entrySet().iterator();
        while (i.hasNext()) {
            final Entry<String, Object> property = i.next();
            final EAttribute attr = (EAttribute) component.eClass().getEStructuralFeature(property.getKey());
            final Object value = ModelProviderUtil.instantiateAttribute(attr.getEAttributeType().getInstanceClass(),
                    property.getValue().toString());

            if (value != null) {
                component.eSet(attr, value);
            }
        }

        // Already register unfinished types because there might be circles with inner declarations
        if (component instanceof DataType) {
            dataTypes.putIfAbsent(node.getId(), (DataType) component);
        }

        // Load related nodes representing referenced components
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS,
                PcmRelationshipType.IS_TYPE)) {

            final Node endNode = rel.getEndNode();
            final String relName = (String) rel.getProperty(ModelProvider.REF_NAME);
            final EReference ref = (EReference) component.eClass().getEStructuralFeature(relName);
            Object refReprensation = component.eGet(ref);

            if (rel.isType(PcmRelationshipType.CONTAINS)) {
                // System.out.println("\t" + component + " reference " + refName);

                if (refReprensation instanceof EList<?>) {
                    final EObject endComponent = this.readComponent(endNode, dataTypes);
                    ((EList<EObject>) refReprensation).add(endComponent);
                } else {
                    refReprensation = this.readComponent(endNode, dataTypes);
                    component.eSet(ref, refReprensation);

                }
            } else if (rel.isType(PcmRelationshipType.IS_TYPE)) {
                // System.out.println("\t" + component + " reference " + refName);

                // Look if this data type has already been created
                EObject endComponent = dataTypes.get(endNode.getId());

                if (endComponent == null) {
                    endComponent = this.readComponent(endNode, dataTypes);
                }

                if (refReprensation instanceof EList<?>) {
                    ((EList<EObject>) refReprensation).add(endComponent);
                } else {
                    component.eSet(ref, endComponent);
                }

                // Replace possibly unfinished data type
                dataTypes.replace(node.getId(), (DataType) endComponent);
            }
        }

        return component;
    }

    /**
     * Reads the ids of all components of a specified data type.
     *
     * @param clazz
     *            The data type
     * @return List of ids of the specified data type
     */
    public List<String> readComponent(final Class<T> clazz) {
        try (Transaction tx = this.getGraph().beginTx()) {
            final ResourceIterator<Node> nodes = this.graph.findNodes(Label.label(clazz.getSimpleName()));
            final LinkedList<String> ids = new LinkedList<>();

            while (nodes.hasNext()) {
                final Node n = nodes.next();
                ids.add(n.getProperty(ModelProvider.ID).toString());
            }

            tx.success();
            return ids;
        }
    }

    /**
     * Updates a specified component in the the provider's {@link #graph}.
     * 
     * @param clazz
     *            Data type of component to be updated
     * @param component
     *            The new component
     */
    public void updateComponent(final Class<T> clazz, final T component) {
        final EAttribute idAttr = component.eClass().getEIDAttribute();
        this.deleteComponent(clazz, component.eGet(idAttr).toString());
        this.createComponent(component);
    }

    /**
     * Deletes a specified component from the provider's {@link #graph}.
     *
     * @param clazz
     *            Data type of component to be deleted
     * @param id
     *            Id of component to be deleted
     */
    public void deleteComponent(final Class<T> clazz, final String id) {
        final Label label = Label.label(clazz.getSimpleName());
        Node node;

        try (Transaction tx = this.getGraph().beginTx()) {
            node = this.getGraph().findNode(label, ModelProvider.ID, id);
            this.markAccessibleNodes(node);
            this.markDeletableNodes(node, true);
            this.deleteNodes(node);
            tx.success();
        }

    }

    /**
     * Helper method for deleting: Starting from a given node this method recursively marks all
     * nodes accessible via {@link PcmRelationshipType#CONTAINS} or
     * {@link PcmRelationshipType#IS_TYPE} edges. Calls to this method have to be performed from
     * inside a {@link Transaction}.
     *
     * @param node
     *            The node to start with
     */
    private void markAccessibleNodes(final Node node) {
        node.setProperty(ModelProvider.DELETE, true);

        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS,
                PcmRelationshipType.IS_TYPE)) {
            if (!rel.hasProperty(ModelProvider.ACCESSIBLE)) {
                rel.setProperty(ModelProvider.ACCESSIBLE, true);
                this.markAccessibleNodes(rel.getEndNode());
            }
        }

        return;
    }

    /**
     * Helper method for deleting: Starting from a given node this method recursively marks all
     * accessible nodes marked with {@link #markAccessibleNodes(Node)} which can be deleted.
     * Starting from one node all contained nodes can be deleted as well. Nodes with incoming
     * {@link PcmRelationshipType#IS_TYPE} edges may only be deleted if they are not referenced from
     * outside the accessible nodes and have no predecessor which is referenced from outside the
     * accessible nodes via an {@link PcmRelationshipType#IS_TYPE} edge. Calls to this method have
     * to be performed from inside a {@link Transaction}.
     *
     * @param node
     *            The node to start with
     * @param reallyDeletePred
     *            Flag if predecessor may be deleted
     */
    private void markDeletableNodes(final Node node, final boolean reallyDeletePred) {

        boolean reallyDelete = reallyDeletePred;

        // Check if there are incoming IS_TYPE relations from outside
        for (final Relationship rel : node.getRelationships(Direction.INCOMING, PcmRelationshipType.IS_TYPE)) {
            if (!rel.hasProperty(ModelProvider.ACCESSIBLE)) {
                reallyDelete = false;
            }
        }

        // Remove delete property if node must not be deleted
        if (node.hasProperty(ModelProvider.DELETE) && !reallyDelete) {
            node.removeProperty(ModelProvider.DELETE);
        }

        // Recursively check successors and mark already visited edges to prevent call circles
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS,
                PcmRelationshipType.IS_TYPE)) {
            if (!rel.hasProperty(ModelProvider.VISITED)) {
                rel.setProperty(ModelProvider.VISITED, true);
                this.markDeletableNodes(rel.getEndNode(), reallyDelete);
            }
        }

        // Remove edge marks when returned from successor node's calls
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS,
                PcmRelationshipType.IS_TYPE)) {
            rel.removeProperty(ModelProvider.VISITED);
        }
    }

    /**
     * Helper method for deleting: Starting from a given node this method recursively traverses down
     * through all accessible nodes marked with {@link #markAccessibleNodes(Node)} and then deletes
     * all nodes marked with a delete flag by {@link #markDeletableNodes(Node)} from bottom to the
     * top. Calls to this method have to be performed from inside a {@link Transaction}.
     *
     * @param node
     *            The node to start with
     */
    private void deleteNodes(final Node node) {

        // Recursively go to the lowest node and mark already visited edges to prevent call circles
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS,
                PcmRelationshipType.IS_TYPE)) {

            try {
                if (!rel.hasProperty(ModelProvider.VISITED)) {
                    rel.setProperty(ModelProvider.VISITED, true);
                    this.deleteNodes(rel.getEndNode());
                }
            } catch (final NotFoundException e) {
                // relation has already been deleted on another path
            }
        }

        try {
            if (node.hasProperty(ModelProvider.DELETE)) {

                // Delete node and its relationships
                for (final Relationship rel : node.getRelationships()) {
                    rel.delete();
                }
                node.delete();

            } else {

                // Only remove visited mark
                for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS,
                        PcmRelationshipType.IS_TYPE)) {
                    rel.removeProperty(ModelProvider.VISITED);
                }
            }
        } catch (final NotFoundException e) {
            // node has already been deleted on another path
        }

        return;
    }

    public GraphDatabaseService getGraph() {
        return this.graph;
    }
}
