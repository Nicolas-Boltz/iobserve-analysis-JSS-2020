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
package org.iobserve.model.test.data;

import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

/**
 * @author Reiner Jung
 *
 */
public final class ResourceEnvironmentDataFactory {

    public static final String RESOURCE_CONTAINER_NAME = "TestResourceContainer";
    public static final String RESOURCE_CONAINTER_ID = "_resourcecontainer_test_id";

    public static final ResourceContainer RESOURCE_CONTAINER = ResourceEnvironmentDataFactory.createResourceContainer();
    public static final ResourceEnvironment RESOURCE_ENVIRONMENT = ResourceEnvironmentDataFactory.createResourceEnvironment();

    private ResourceEnvironmentDataFactory() {
		// private empty constructor for factory
	}
    
    private static ResourceContainer createResourceContainer() {
        /** optional test resource container with value */
        final ResourceContainer container = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();

        container.setEntityName(ResourceEnvironmentDataFactory.RESOURCE_CONTAINER_NAME);
        container.setId(ResourceEnvironmentDataFactory.RESOURCE_CONAINTER_ID);

        return container;
    }

    private static ResourceEnvironment createResourceEnvironment() {
        return ResourceenvironmentFactory.eINSTANCE.createResourceEnvironment();
    }
}
