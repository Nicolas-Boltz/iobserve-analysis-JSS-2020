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
package org.iobserve.execution.stages.kubernetes;

import org.iobserve.adaptation.executionplan.AtomicAction;
import org.iobserve.execution.stages.IExecutor;

/**
 *
 * @author Lars Bluemke
 *
 * @param <T>
 */
public abstract class AbstractExecutor<T extends AtomicAction> implements IExecutor<T> {

    /**
     * Normalizes component names to conform to the kubernetes standard which uses this regex for
     * validation: '[a-z0-9]([-a-z0-9]*[a-z0-9])?').
     *
     * @param componentName
     *            The component's original name
     * @return The normalized name
     */
    protected String normalizeComponentName(final String componentName) {
        return componentName.toLowerCase().replaceAll("[^a-z0-9\\-]", "");
    }
}
