package org.iobserve.analysis.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iobserve.analysis.InitializeModelProviders;

/**
 * This class contains a model for privacy analysis purposes.
 *
 * @author Philipp Weimann
 *
 */
public class ModelGraph {
    protected static final Logger LOG = LogManager.getLogger(ModelGraph.class);

    private final Set<DeploymentNode> servers;
    private final Set<ComponentNode> components;
    private final InitializeModelProviders pcmModels;

    public ModelGraph(final Collection<DeploymentNode> servers, final Collection<ComponentNode> components,
            final InitializeModelProviders pcmModels) {
        this.servers = new HashSet<>(servers);
        this.components = new HashSet<>(components);
        this.pcmModels = pcmModels;
        // this.printGraph();
    }

    public void printGraph() {
        for (final DeploymentNode server : this.getServers()) {
            ModelGraph.LOG.info(server.toString());
        }
    }

    /**
     * @return the servers
     */
    public Set<DeploymentNode> getServers() {
        return this.servers;
    }

    /**
     * @return the components
     */
    public Set<ComponentNode> getComponents() {
        return this.components;
    }

    /**
     * @return the pcmModels
     */
    public InitializeModelProviders getPcmModels() {
        return this.pcmModels;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ModelGraph) {
            final ModelGraph compObj = (ModelGraph) obj;

            // Compare used servers
            boolean equalServers = true;
            for (final DeploymentNode server : this.servers) {
                if (server.getContainingComponents().size() == 0) {
                    continue;
                }

                final DeploymentNode compServer = compObj.servers.stream()
                        .filter(s -> s.getResourceContainerID().equals(server.getResourceContainerID())).findFirst()
                        .get();
                equalServers = server.equals(compServer);
                if (!equalServers) {
                    return false;
                }
            }

            // Compare components
            boolean equalComponents = true;
            for (final ComponentNode component : this.components) {
                final ComponentNode compComponent = compObj.components.stream()
                        .filter(s -> s.getAssemblyContextID().equals(component.getAssemblyContextID())).findFirst()
                        .get();
                equalComponents = component.equals(compComponent);
                if (!equalComponents) {
                    return false;
                }
            }
        }
        return true;
    }
}
