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
package org.iobserve.service.privacy.violation.filter;

import java.io.File;

import teetime.framework.AbstractConsumerStage;

import org.iobserve.service.privacy.violation.data.Warnings;

/**
 * @author Reiner jung
 *
 */
public class WarnSink extends AbstractConsumerStage<Warnings> {

    public WarnSink(final File warningFile) {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void execute(final Warnings element) throws Exception {
        System.out.println("Warnings");
        for (final String warning : element.getWarnings()) {
            System.out.println("\t" + warning);
        }
    }

}
