package org.iobserve.analysis.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonWriter;

import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.neo4j.graphdb.GraphDatabaseService;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 *
 * @author jweg
 *
 */
public final class InitializeDeploymentVisualization {

    /** reference to allocation model graph. */
    private final GraphDatabaseService allocationModelGraph;
    /** reference to system model graph. */
    private final GraphDatabaseService systemModelGraph;
    /** reference to resource environment model graph. */
    private final GraphDatabaseService resourceEnvironmentModelGraph;

    private static final String USER_AGENT = "iObserve/0.0.2";

    private final URL changelogURL;
    private final URL systemURL;

    /**
     * constructor
     *
     * @param allocationModelProvider
     * @param systemModelProvider
     * @param resourceEnvironmentModelProvider
     */

    public InitializeDeploymentVisualization(final URL systemURL, final URL changelogURL,
            final GraphDatabaseService allocationModelGraph, final GraphDatabaseService systemModelGraph,
            final GraphDatabaseService resourceEnvironmentModelGraph) {
        // get all model references
        this.systemURL = systemURL;
        this.changelogURL = changelogURL;
        this.allocationModelGraph = allocationModelGraph;
        this.systemModelGraph = systemModelGraph;
        this.resourceEnvironmentModelGraph = resourceEnvironmentModelGraph;
    }

    protected void initialize() throws Exception {
        // this.sendPostRequest(this.createSystem());
        System.out.println("createSystem()");
        this.sendPostRequest(this.createChangelog());
        System.out.println("createChangelog()");

    }

    /**
     *
     * @return
     */
    private JsonArray createSystem() {
        final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelProvider = new ModelProvider<>(
                this.systemModelGraph);

        final org.palladiosimulator.pcm.system.System systemModel = systemModelProvider
                .readRootComponent(org.palladiosimulator.pcm.system.System.class);
        final String systemId = systemModel.getId();
        final String systemName = systemModel.getEntityName();

        final JsonObject system = Json.createObjectBuilder().add("type", "system").add("id", systemId)
                .add("name", systemName).build();
        final JsonArray systemData = Json.createArrayBuilder().add(system).build();

        return systemData;
    }

    /**
     *
     * @return
     */
    private JsonArray createChangelog() {
        final JsonArrayBuilder nodeArrayBuilder = Json.createArrayBuilder();

        final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelProvider = new ModelProvider<>(
                this.systemModelGraph);
        final org.palladiosimulator.pcm.system.System systemModel = systemModelProvider
                .readRootComponent(org.palladiosimulator.pcm.system.System.class);
        final String systemId = systemModel.getId();

        final ModelProvider<Allocation> allocationModelProvider = new ModelProvider<>(this.allocationModelGraph);
        final List<String> allocationIds = allocationModelProvider.readComponentByType(Allocation.class);
        final String allocationId = allocationIds.get(0);
        final Allocation allocation = allocationModelProvider.readComponentById(Allocation.class, allocationId);
        final List<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();

        for (int i = 0; i < allocationContexts.size(); i++) {
            final AllocationContext allocationContext = allocationContexts.get(0);

            // resourceContainer
            final ResourceContainer resourceContainer = allocationContext.getResourceContainer_AllocationContext();

            final String resourceContainerId = resourceContainer.getId();
            final String hostname = resourceContainer.getEntityName();
            // as there is no grouping element available, each node gets its own nodegroup with a
            // random id
            final String nodeGroupId = "nodeGroup-" + Math.random();
            final JsonObject nodeGroup = Json.createObjectBuilder().add("type", "nodeGroup").add("id", nodeGroupId)
                    .add("systemId", systemId).add("name", "nodeGroupName").build();
            final JsonObject nodeGroupData = Json.createObjectBuilder().add("type", "changelog")
                    .add("operation", "CREATE").add("data", nodeGroup).build();
            final JsonObject node = Json.createObjectBuilder().add("type", "node").add("id", resourceContainerId)
                    .add("systemId", systemId).add("nodeGroupId", nodeGroupId).add("hostname", hostname).build();
            final JsonObject nodeData = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                    .add("data", node).build();
            // assemblyContext
            final AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();

            final String assemblyContextId = assemblyContext.getId();
            final String assemblyContextName = assemblyContext.getEntityName();

            final JsonObject service = Json.createObjectBuilder().add("type", "service").add("id", assemblyContextId)
                    .add("systemId", systemId).add("name", assemblyContextName).build();
            final JsonObject serviceData = Json.createObjectBuilder().add("type", "changelog")
                    .add("operation", "CREATE").add("data", service).build();
            final JsonObject serviceInstance = Json.createObjectBuilder().add("type", "serviceInstance")
                    .add("id", "serviceInstance-id-analysis").add("systemId", systemId)
                    .add("name", "analysis-serviceInstance").add("serviceId", assemblyContextId)
                    .add("nodeId", resourceContainerId).build();
            final JsonObject serviceInstanceData = Json.createObjectBuilder().add("type", "changelog")
                    .add("operation", "CREATE").add("data", serviceInstance).build();
            // das geht nicht, aber nodeGroup einzeln geht
            nodeArrayBuilder.add(nodeGroupData).add(nodeData).add(serviceData).add(serviceInstanceData);
        }

        // communication data

        // communication instance data

        final JsonArray dataArray = nodeArrayBuilder.build();
        System.out.printf("after for, nodeArray:%s\n", dataArray);
        return dataArray;
    }

    /**
     * Send change log updates to the visualization.
     *
     * @param modelData
     * @throws IOException
     */
    private void sendPostRequest(final JsonArray modelData) throws IOException {
        final HttpURLConnection connection;
        System.out.printf("system?:%s\n", modelData.get(0));
        final JsonObject obj = modelData.getJsonObject(0);
        final JsonString type = (JsonString) obj.get("type");
        System.out.printf("type:%s\n", type);
        if (type.getString() == "system") {
            connection = (HttpURLConnection) this.systemURL.openConnection();
        } else {
            connection = (HttpURLConnection) this.changelogURL.openConnection();
        }

        // add request header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        connection.setRequestProperty("User-Agent", InitializeDeploymentVisualization.USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        // Send post request
        connection.setDoOutput(true);
        final JsonWriter jsonWriter = Json.createWriter(connection.getOutputStream());
        if (type.getString() == "system") {

            System.out.printf("obj for JsonWriter:%s\n", obj);
            jsonWriter.write(obj);
            jsonWriter.close();

            System.out.println("\nSending 'POST' request to URL : " + this.systemURL);
            System.out.println("Post parameters : " + obj);

        } else {
            for (int i = 0; i < modelData.size(); i++) {
                System.out.printf("modelData.getJsonObject(i):%s\n", modelData.getJsonObject(i));
                jsonWriter.write(modelData.getJsonObject(i)); // work in progress
            }

            System.out.println("\nSending 'POST' request to URL : " + this.changelogURL);
            System.out.println("Post parameters : " + modelData);
        }
        jsonWriter.close();
        final int responseCode = connection.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        final StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        System.out.println(response.toString());

    }

}
