/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.adaptation.configurations;

import org.iobserve.adaptation.AdaptationCalculation;
import org.iobserve.adaptation.AdaptationPlanning;
import org.iobserve.adaptation.ModelProducer;
import org.iobserve.adaptation.SystemAdaptation;
import org.iobserve.stages.source.SingleConnectionTcpWriterStage;

import teetime.framework.Configuration;

/**
 * Configuration for the stages of the adaptation service.
 *
 * @author Lars Bluemke
 *
 */
public class AdaptationConfiguration extends Configuration {

    public AdaptationConfiguration() {
        final SystemAdaptation systemAdaptor = new SystemAdaptation(new AdaptationCalculation(),
                new AdaptationPlanning());

        // TODO for lbl: Implement a way to pass data to the following stages
        // Path Adaptation => Execution
        // this.connectPorts(systemAdaptor.getOutputPort(), adaptationExecution.getInputPort());

        // Debugging
        final ModelProducer modelProducer = new ModelProducer();
        final SingleConnectionTcpWriterStage modelWriter = new SingleConnectionTcpWriterStage("localhost", 12345);

        this.connectPorts(modelProducer.getOutputPort(), modelWriter.getInputPort());
    }

}