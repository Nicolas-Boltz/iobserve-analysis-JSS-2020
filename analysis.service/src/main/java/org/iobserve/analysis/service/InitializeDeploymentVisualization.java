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
package org.iobserve.analysis.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.service.services.CommunicationInstanceService;
import org.iobserve.analysis.service.services.CommunicationService;
import org.iobserve.analysis.service.services.NodeService;
import org.iobserve.analysis.service.services.NodegroupService;
import org.iobserve.analysis.service.services.ServiceInstanceService;
import org.iobserve.analysis.service.services.ServiceService;
import org.iobserve.analysis.service.services.SystemService;
import org.iobserve.analysis.service.services.UsergroupService;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.impl.LinkingResourceImpl;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

import util.Changelog;
import util.SendHttpRequest;

/**
 * Initializes the deployment visualization by mapping the initial palladio components to
 * visualization elements.
 *
 * @author jweg
 *
 */
public final class InitializeDeploymentVisualization {

    /** model provider for palladio models. */
    private final ModelProvider<Allocation> allocationModelGraphProvider;
    private final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider;
    private final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;
    private final ModelProvider<UsageModel> usageModelGraphProvider;

    /** services for visualization elements. */
    private final SystemService systemService = new SystemService();
    private final NodegroupService nodegroupService = new NodegroupService();
    private final NodeService nodeService = new NodeService();
    private final ServiceService serviceService = new ServiceService();
    private final ServiceInstanceService serviceinstanceService = new ServiceInstanceService();
    private final CommunicationService communicationService = new CommunicationService();
    private final CommunicationInstanceService communicationinstanceService = new CommunicationInstanceService();
    private final UsergroupService usergroupService = new UsergroupService();

    private final URL changelogUrl;
    private final URL systemUrl;

    /**
     * constructor.
     *
     * @param systemUrl
     *            Url to send requests for creating a system
     * @param changelogUrl
     *            Url to send requests for changelogs
     * @param allocationModelGraphProvider
     *            provider for allocation model
     * @param systemModelGraphProvider
     *            provider for system model
     * @param resourceEnvironmentModelGraphProvider
     *            provider for resource environment model
     * @param usageModelGraphProvider
     *            provider for usage model
     */

    public InitializeDeploymentVisualization(final URL systemUrl, final URL changelogUrl,
            final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider,
            final ModelProvider<UsageModel> usageModelGraphProvider) {
        this.systemUrl = systemUrl;
        this.changelogUrl = changelogUrl;
        this.allocationModelGraphProvider = allocationModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
        this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
        this.usageModelGraphProvider = usageModelGraphProvider;
    }

    /**
     * Populates the database of the deployment visualization initially and respects the changelog
     * constraints of iobserve-ui-deployment. It takes information from the system model, the
     * allocation model and the resource environment model and creates corresponding visualization
     * components, e.g. nodes and services.
     *
     * @throws Exception
     *             when post request fails
     */
    protected void initialize() throws Exception {
        // set up the system model and take parts from it
        final org.palladiosimulator.pcm.system.System systemModel = this.systemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);
        final List<AssemblyContext> assemblyContexts = systemModel.getAssemblyContexts__ComposedStructure();

        // set up the allocation model and take parts from it
        final List<String> allocationIds = this.allocationModelGraphProvider.readComponentByType(Allocation.class);
        // an allocation model contains exactly one allocation, therefore .get(0)
        final String allocationId = allocationIds.get(0);
        final Allocation allocation = this.allocationModelGraphProvider.readOnlyComponentById(Allocation.class,
                allocationId);
        final List<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();

        // set up the resource environment model and take parts from it
        final ResourceEnvironment resourceEnvironmentModel = this.resourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class);
        final List<LinkingResource> linkingResources = resourceEnvironmentModel
                .getLinkingResources__ResourceEnvironment();
        final List<ResourceContainer> resourceContainers = resourceEnvironmentModel
                .getResourceContainer_ResourceEnvironment();

        // set up the usage model and take parts from it
        final UsageModel usageModel = this.usageModelGraphProvider.readOnlyRootComponent(UsageModel.class);
        final List<UsageScenario> usageScenarios = usageModel.getUsageScenario_UsageModel();

        // sending created components to visualization (in predefined order stated in changelog
        // constraints)
        /** system */
        SendHttpRequest.post(this.systemService.createSystem(systemModel), this.systemUrl, this.changelogUrl);

        /** node group and node */
        for (int i = 0; i < resourceContainers.size(); i++) {
            final ResourceContainer resourceContainer = resourceContainers.get(i);
            SendHttpRequest.post(
                    Changelog.create(this.nodegroupService.createNodegroup(this.systemService.getSystemId())),
                    this.systemUrl, this.changelogUrl);
            SendHttpRequest.post(Changelog.create(this.nodeService.createNode(resourceContainer,
                    this.systemService.getSystemId(), this.nodegroupService.getNodegroupId())), this.systemUrl,
                    this.changelogUrl);
        }

        /** service and service instance */
        for (int i = 0; i < assemblyContexts.size(); i++) {
            final AssemblyContext assemblyContext = assemblyContexts.get(i);
            SendHttpRequest.post(
                    Changelog.create(
                            this.serviceService.createService(assemblyContext, this.systemService.getSystemId())),
                    this.systemUrl, this.changelogUrl);
        }

        for (int i = 0; i < allocationContexts.size(); i++) {
            final AllocationContext allocationContext = allocationContexts.get(i);

            final String resourceContainerId = allocationContext.getResourceContainer_AllocationContext().getId();
            final AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();
            final String assemblyContextId = allocationContext.getAssemblyContext_AllocationContext().getId();

            SendHttpRequest.post(
                    Changelog.create(this.serviceinstanceService.createServiceInstance(assemblyContext,
                            this.systemService.getSystemId(), resourceContainerId, assemblyContextId)),
                    this.systemUrl, this.changelogUrl);
        }

        /** communication and communication instance */

        final List<Connector> connectors = systemModel.getConnectors__ComposedStructure();

        for (int i = 0; i < connectors.size(); i++) {
            final Connector connector = connectors.get(i);
            // we are only interested in AssemblyConnectors
            if (connector instanceof AssemblyConnector) {

                final String technology = this.getTechnology((AssemblyConnector) connector, linkingResources);

                SendHttpRequest.post(
                        Changelog.create(this.communicationService.createCommunication((AssemblyConnector) connector,
                                this.systemService.getSystemId(), technology)),
                        this.systemUrl, this.changelogUrl);

                SendHttpRequest.post(
                        Changelog.create(this.communicationinstanceService.createCommunicationInstance(
                                (AssemblyConnector) connector, this.systemService.getSystemId(),
                                this.communicationService.getCommunicationId())),
                        this.systemUrl, this.changelogUrl);

            } else {
                System.out.printf("no AssemblyConnector: %s\n", connector.getEntityName());
            }

        }

        /** usergroup */
        // not working yet

        // map elements in entryLevelSystemCalls to assemblyContexts

        // final List<AssemblyContext> userInvokedServices = new ArrayList<>();
        // List<EntryLevelSystemCall> entryLevelSystemCalls = new ArrayList<>();
        //
        // entryLevelSystemCalls = this.collectEntryLevelSystemCalls(usageScenarios);
        //
        // for (int m = 0; m < entryLevelSystemCalls.size(); m++) {
        // final EntryLevelSystemCall userStep = entryLevelSystemCalls.get(m);
        //
        // final String providedRoleId = userStep.getProvidedRole_EntryLevelSystemCall().getId();
        //
        // final List<EObject> usergroupConnectors = this.systemModelGraphProvider
        // .readOnlyReferencingComponentsById(OperationProvidedRole.class, providedRoleId);
        // final ProvidedDelegationConnectorImpl usergroupConnector =
        // (ProvidedDelegationConnectorImpl) usergroupConnectors
        // .get(0);
        //
        // final AssemblyContext assemblyContext =
        // usergroupConnector.getAssemblyContext_ProvidedDelegationConnector();
        //
        // userInvokedServices.add(assemblyContext);
        // }

        // if (userInvokedServices.size() > 0) {
        // SendHttpRequest.post(Changelog.create(
        // this.usergroupService.createUsergroup(this.systemService.getSystemId(),
        // userInvokedServices)),
        // this.systemUrl, this.changelogUrl);
        //
        // }

    }

    /**
     * Help function for getting the technology used for communication. Therefore get the
     * {@link ResourceContainer}s on which the two communicating {@link AssemblyContext}s are
     * deployed. This information is stored in the {@link Allocation} model. Then find the
     * {@link LinkingResource} in the {@link ResourceEnvironment} model, that connect these
     * {@link ResourceContainer}s.
     *
     * @param connector
     *            assembly connector
     * @param linkingResources
     *            list of linking resources
     * @return technology used for communication, entity name of linking resource
     */

    private String getTechnology(final AssemblyConnector connector, final List<LinkingResource> linkingResources) {
        /**
         * ID of resource container on which source (regarding communication) assembly context is
         * deployed
         */
        String resourceSourceId = null;
        /**
         * ID of resource container on which target (regarding communication) assembly context is
         * deployed
         */
        String resourceTargetId = null;
        /** technology of communication */
        String technology = null;

        final String assemContSourceId = connector.getProvidingAssemblyContext_AssemblyConnector().getId();
        final String assemContTargetId = connector.getRequiringAssemblyContext_AssemblyConnector().getId();

        final List<EObject> allocationContextsWithSource = this.allocationModelGraphProvider
                .readOnlyReferencingComponentsById(AssemblyContext.class, assemContSourceId);
        if (allocationContextsWithSource.get(0) instanceof AllocationContext) {
            final AllocationContext allocationContext = (AllocationContext) allocationContextsWithSource.get(0);
            resourceSourceId = allocationContext.getResourceContainer_AllocationContext().getId();
        }

        final List<EObject> allocationContextsWithTarget = this.allocationModelGraphProvider
                .readOnlyReferencingComponentsById(AssemblyContext.class, assemContTargetId);
        if (allocationContextsWithTarget.get(0) instanceof AllocationContext) {
            final AllocationContext allocationContext = (AllocationContext) allocationContextsWithTarget.get(0);
            resourceTargetId = allocationContext.getResourceContainer_AllocationContext().getId();
        }

        if ((resourceSourceId != null) && (resourceTargetId != null)) {
            for (int l = 0; l < linkingResources.size(); l++) {
                final LinkingResource linkingResource = linkingResources.get(l);
                if (linkingResource instanceof LinkingResourceImpl) {
                    final List<ResourceContainer> connectedResourceConts = linkingResource
                            .getConnectedResourceContainers_LinkingResource();
                    final List<String> connectedResourceContsIds = new ArrayList<>();
                    for (int k = 0; k < connectedResourceConts.size(); k++) {
                        connectedResourceContsIds.add(connectedResourceConts.get(k).getId());
                    }
                    if (connectedResourceContsIds.contains(resourceSourceId)) {
                        if (connectedResourceContsIds.contains(resourceTargetId)) {
                            technology = linkingResource.getEntityName();
                        }
                    }
                }
            }
        }
        return technology;
    }

    /**
     * Help function for taking all {@link EntryLevelSystemCall}s from a {@link UsageScenario}.
     * {@link EntryLevelSystemCall}s with the same {@link ProvidedRole} are added just once. This
     * list will be passed to the {@link UsergroupService}.
     *
     * @param usageScenarios
     *            list of usage scenarios
     * @return list of EntryLevelSystemCalls
     */

    private List<EntryLevelSystemCall> collectEntryLevelSystemCalls(final List<UsageScenario> usageScenarios) {

        final List<EntryLevelSystemCall> entryLevelSystemCalls = new ArrayList<>();

        for (int h = 0; h < usageScenarios.size(); h++) {
            final UsageScenario usageScenario = usageScenarios.get(h);

            final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
            final List<AbstractUserAction> usageActions = scenarioBehaviour.getActions_ScenarioBehaviour();

            for (int i = 0; i < usageActions.size(); i++) {
                final AbstractUserAction actualUsageAction = usageActions.get(i);

                if ((actualUsageAction instanceof EntryLevelSystemCall) && !(this.containsProvidedRole(
                        entryLevelSystemCalls,
                        ((EntryLevelSystemCall) actualUsageAction).getProvidedRole_EntryLevelSystemCall().getId()))) {
                    entryLevelSystemCalls.add((EntryLevelSystemCall) actualUsageAction);

                }
                if (actualUsageAction instanceof Branch) {
                    final List<BranchTransition> branches = ((Branch) actualUsageAction).getBranchTransitions_Branch();
                    AbstractUserAction branchAction;
                    List<AbstractUserAction> branchActions;

                    for (int j = 0; j < branches.size(); j++) {
                        final BranchTransition branchTransition = branches.get(j);
                        branchActions = branchTransition.getBranchedBehaviour_BranchTransition()
                                .getActions_ScenarioBehaviour();
                        for (int k = 0; k < branchActions.size(); k++) {
                            branchAction = branchActions.get(k);

                            if ((branchAction instanceof EntryLevelSystemCall) && !(this
                                    .containsProvidedRole(entryLevelSystemCalls, ((EntryLevelSystemCall) branchAction)
                                            .getProvidedRole_EntryLevelSystemCall().getId()))) {
                                entryLevelSystemCalls.add((EntryLevelSystemCall) branchAction);
                            }
                        }
                    }
                }
                if (actualUsageAction instanceof Loop) {
                    final ScenarioBehaviour loopBehaviour = ((Loop) actualUsageAction).getBodyBehaviour_Loop();
                    final List<AbstractUserAction> loopActions = loopBehaviour.getActions_ScenarioBehaviour();

                    for (int l = 0; l < loopActions.size(); l++) {
                        final AbstractUserAction loopAction = loopActions.get(l);
                        System.out.printf("loopAction:%s\n", loopAction);
                        if ((loopAction instanceof EntryLevelSystemCall) && !(this.containsProvidedRole(
                                entryLevelSystemCalls,
                                ((EntryLevelSystemCall) loopAction).getProvidedRole_EntryLevelSystemCall().getId()))) {
                            entryLevelSystemCalls.add((EntryLevelSystemCall) loopAction);
                        }
                    }
                }

            }
        }
        return entryLevelSystemCalls;
    }

    /**
     * Help function for adding {@link EntryLevelSystemCall}s with new {@link ProvidedRole}.
     *
     * @param entryLevelSystemCalls
     *            list of containing EntryLevelSystemCalls
     * @param providedRoleId
     *            Id of provided role
     * @return true, if an element in the list has the same provided role as the new element, else:
     *         false
     */
    private boolean containsProvidedRole(final List<EntryLevelSystemCall> entryLevelSystemCalls,
            final String providedRoleId) {
        for (final EntryLevelSystemCall o : entryLevelSystemCalls) {
            if ((o != null) && o.getProvidedRole_EntryLevelSystemCall().getId().equals(providedRoleId)) {
                return true;
            }
        }
        return false;
    }

}