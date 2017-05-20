package org.iobserve.adaption.data;

import org.iobserve.analysis.graph.ComponentNode;
import org.iobserve.analysis.graph.DeploymentNode;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.TerminateAction;
import org.iobserve.planning.systemadaptation.systemadaptationFactory;
import org.iobserve.planning.systemadaptation.impl.systemadaptationFactoryImpl;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * This class provides a factory for system adaption Actions. It provides all
 * basic functions. Initialize the static fields {@value runtimeModels} and
 * {@value redeploymentModels} before using this class.
 * 
 * @author Philipp Weimann
 */
public class RessourceContainerActionFactory extends ActionFactory {

	private static ResourceContainerAction setSourceResourceContainer(ResourceContainerAction action, String resourceContainerID) {
		ResourceEnvironment resEnvModel = ActionFactory.runtimeModels.getResourceEnvironmentModelProvider().getModel();
		ResourceContainer resourceContainer = ActionFactory.getResourceContainer(resourceContainerID, resEnvModel);
		action.setSourceResourceContainer(resourceContainer);
		return action;
	}

	public static TerminateAction generateTerminateAction(DeploymentNode runtimeServer) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		TerminateAction action = factory.createTerminateAction();

		RessourceContainerActionFactory.setSourceResourceContainer(action, runtimeServer.getResourceContainerID());

		return action;
	}
	
	public static AcquireAction generateAcquireAction(DeploymentNode reDeploymentServer) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		AcquireAction action = factory.createAcquireAction();

		RessourceContainerActionFactory.setSourceResourceContainer(action, reDeploymentServer.getResourceContainerID());

		return action;
	}

	public static ReplicateAction generateReplicateAction(DeploymentNode runtimeServer, DeploymentNode reDeploymentServer) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		ReplicateAction action = factory.createReplicateAction();

		RessourceContainerActionFactory.setSourceResourceContainer(action, runtimeServer.getResourceContainerID());

		Allocation runtimeAllocModel = ActionFactory.runtimeModels.getAllocationModelProvider().getModel();
		for (ComponentNode component : runtimeServer.getContainingComponents()) {
			AllocationContext oldAllocationContext = ActionFactory.getAllocationContext(component.getAllocationContextID(), runtimeAllocModel);
			action.getSourceAllocationContext().add(oldAllocationContext);
		}

		Allocation reDeplAllocModel = ActionFactory.redeploymentModels.getAllocationModelProvider().getModel();
		for (ComponentNode component : reDeploymentServer.getContainingComponents()) {
			AllocationContext newAllocationContext = ActionFactory.getAllocationContext(component.getAllocationContextID(), reDeplAllocModel);
			action.getSourceAllocationContext().add(newAllocationContext);
		}

		ResourceEnvironment resEnvModel = redeploymentModels.getResourceEnvironmentModelProvider().getModel();
		ResourceContainer newResourceContainer = ActionFactory.getResourceContainer(reDeploymentServer.getResourceContainerID(), resEnvModel);
		action.setNewResourceContainer(newResourceContainer);

		return action;
	}

}
