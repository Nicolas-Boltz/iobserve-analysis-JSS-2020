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
package org.iobserve.adaptation.data;

import org.iobserve.analysis.data.graph.ComponentNode;
import org.iobserve.analysis.data.graph.DeploymentNode;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.SystemadaptationFactory;
import org.iobserve.planning.systemadaptation.TerminateAction;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * This class provides a factory for system adaption Actions. It provides all basic functions.
 * Initialize the static fields {@value runtimeModels} and {@value redeploymentModels} before using
 * this class.
 *
 * @author Philipp Weimann
 */
public final class ResourceContainerActionFactory {

    private ResourceContainerActionFactory() {
        // private factory constructor
    }

    private static ResourceContainerAction setSourceResourceContainer(final ResourceContainerAction action,
            final String resourceContainerID) {
        final ResourceEnvironment resEnvModel = ActionFactory.getRuntimeModels().getResourceEnvironmentModel();
        final ResourceContainer resourceContainer = ActionFactory.getResourceContainer(resourceContainerID,
                resEnvModel);
        action.setSourceResourceContainer(resourceContainer);
        return action;
    }

    /**
     * Create a terminate action.
     *
     * @param runtimeServer
     *            the node going to be terminated
     * @return the action
     */
    public static TerminateAction createTerminateAction(final DeploymentNode runtimeServer) {
        final SystemadaptationFactory factory = SystemadaptationFactory.eINSTANCE;
        final TerminateAction action = factory.createTerminateAction();

        ResourceContainerActionFactory.setSourceResourceContainer(action, runtimeServer.getResourceContainerID());

        return action;
    }

    /**
     * Create a acquire action.
     *
     * @param reDeploymentServer
     *            the node where components can be deployed on
     * @return the action
     */
    public static AcquireAction createAcquireAction(final DeploymentNode reDeploymentServer) {
        final SystemadaptationFactory factory = SystemadaptationFactory.eINSTANCE;
        final AcquireAction action = factory.createAcquireAction();

        final ResourceEnvironment reDeplResEnvModel = ActionFactory.getRedeploymentModels()
                .getResourceEnvironmentModel();
        final ResourceContainer resourceContainer = ActionFactory
                .getResourceContainer(reDeploymentServer.getResourceContainerID(), reDeplResEnvModel);
        action.setSourceResourceContainer(resourceContainer);

        return action;
    }

    /**
     * Create replicate action.
     *
     * @param runtimeServer
     *            source server
     * @param reDeploymentServer
     *            target server
     * @return the action
     */
    public static ReplicateAction createReplicateAction(final DeploymentNode runtimeServer,
            final DeploymentNode reDeploymentServer) {
        final SystemadaptationFactory factory = SystemadaptationFactory.eINSTANCE;
        final ReplicateAction action = factory.createReplicateAction();

        ResourceContainerActionFactory.setSourceResourceContainer(action, runtimeServer.getResourceContainerID());

        final Allocation runtimeAllocModel = ActionFactory.getRuntimeModels().getAllocationModel();
        for (final ComponentNode component : runtimeServer.getContainingComponents()) {
            final AllocationContext oldAllocationContext = ActionFactory
                    .getAllocationContext(component.getAllocationContextID(), runtimeAllocModel);
            action.getSourceAllocationContext().add(oldAllocationContext);
        }

        final Allocation reDeplAllocModel = ActionFactory.getRedeploymentModels().getAllocationModel();
        for (final ComponentNode component : reDeploymentServer.getContainingComponents()) {
            final AllocationContext newAllocationContext = ActionFactory
                    .getAllocationContext(component.getAllocationContextID(), reDeplAllocModel);
            action.getSourceAllocationContext().add(newAllocationContext);
        }

        final ResourceEnvironment resEnvModel = ActionFactory.getRedeploymentModels().getResourceEnvironmentModel();
        final ResourceContainer newResourceContainer = ActionFactory
                .getResourceContainer(reDeploymentServer.getResourceContainerID(), resEnvModel);
        action.setNewResourceContainer(newResourceContainer);

        return action;
    }

}
