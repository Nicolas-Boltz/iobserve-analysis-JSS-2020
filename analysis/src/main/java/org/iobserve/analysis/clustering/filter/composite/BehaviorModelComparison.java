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
package org.iobserve.analysis.clustering.filter.composite;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.distributor.strategy.IDistributorStrategy;

import org.iobserve.analysis.clustering.filter.models.configuration.BehaviorModelConfiguration;
import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * Distribute EntryCallSequences to both BehaviorModel generations for evaluation purposes.
 *
 *
 * @author Christoph Dornieden
 *
 */
public class BehaviorModelComparison extends CompositeStage {

    private final Distributor<EntryCallSequenceModel> distributor;

    /**
     * constructor.
     *
     * @param configuration
     *            configuration
     * @param correspondenceModel
     *            correspondenceModel
     * @param usageModelProvider
     *            usageModelProvicer
     * @param repositoryModelProvider
     *            repositoryModelProvider
     * @param varianceOfUserGroups
     *            varianceOfUserGroups
     * @param thinkTime
     *            thinkTime
     * @param closedWorkload
     *            closedWorkload
     */

    public BehaviorModelComparison(final BehaviorModelConfiguration configuration,
            final ICorrespondence correspondenceModel, final ModelProvider<UsageModel> usageModelProvider,
            final ModelProvider<Repository> repositoryModelProvider, final int varianceOfUserGroups,
            final int thinkTime, final boolean closedWorkload) {

        // cdor userbehavior
        final IDistributorStrategy strategy = new CopyByReferenceStrategy();
        this.distributor = new Distributor<>(strategy);

        final TBehaviorModel tBehaviorModel = new TBehaviorModel(configuration);

        this.connectPorts(this.distributor.getNewOutputPort(), tBehaviorModel.getInputPort());

        // TODO make both options available. use different composite stages for it.
        // iobserve user behavior
        // final TEntryEventSequence tEntryEventSequence = new
        // TEntryEventSequence(correspondenceModel, usageModelProvider,
        // repositoryModelProvider, varianceOfUserGroups, thinkTime, closedWorkload);
        //
        // final TUsageModelToBehaviorModel tUsageModel = new TUsageModelToBehaviorModel();
        // final TBehaviorModelVisualization tIObserveUBM = new TBehaviorModelVisualization(
        // configuration.getVisualizationUrl(), configuration.getSignatureCreationStrategy());
        // this.connectPorts(this.distributor.getNewOutputPort(),
        // tEntryEventSequence.getInputPort());
        // this.connectPorts(tEntryEventSequence.getOutputPort(), tUsageModel.getInputPort());
        // this.connectPorts(tUsageModel.getOutputPort(), tIObserveUBM.getInputPort());

    }

    public InputPort<EntryCallSequenceModel> getInputPort() {
        return this.distributor.getInputPort();
    }

}
