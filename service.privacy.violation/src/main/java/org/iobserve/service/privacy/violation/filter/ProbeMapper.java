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
package org.iobserve.service.privacy.violation.filter;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.service.privacy.violation.data.ProbeLocation;

/**
 * Translate model level probe control events to code level events.
 *
 * @author Reiner Jung
 *
 */
public class ProbeMapper extends AbstractConsumerStage<ProbeLocation> {

    private final OutputPort<String> outputPort = this.createOutputPort();
    private final ICorrespondence rac;

    /**
     * Initialize probe mapper from model to code level.
     *
     * @param rac
     *            correspondence model used for mapping
     */
    public ProbeMapper(final ICorrespondence rac) {
        this.rac = rac;
    }

    @Override
    protected void execute(final ProbeLocation element) throws Exception {
        // TODO Auto-generated method stub

    }

    public OutputPort<String> getOutputPort() {
        return this.outputPort;
    }

}