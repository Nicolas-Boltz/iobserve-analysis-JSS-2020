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
package org.iobserve.evaluation.filter;

import java.io.File;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;

import com.fasterxml.jackson.databind.ObjectMapper;

import teetime.framework.AbstractProducerStage;

/**
 * Read a JSON serialized behavior model.
 *
 * @author Reiner Jung
 *
 */
public class BehaviorModelJSONReader extends AbstractProducerStage<BehaviorModel> {

    private final File inputFile;

    /**
     * Create a JSON reader stage.
     *
     * @param inputFile
     *            JSON file source
     */
    public BehaviorModelJSONReader(final File inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    protected void execute() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();

        final BehaviorModel model = mapper.readValue(this.inputFile, BehaviorModel.class);

        this.outputPort.send(model);

        this.workCompleted();
    }

}
