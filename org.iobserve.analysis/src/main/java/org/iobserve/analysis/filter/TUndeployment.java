/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.filter;

import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.common.record.IUndeploymentRecord;

import teetime.framework.AbstractConsumerStage;

/**
 * It could be interesting to combine DeploymentEventTransformation and
 * UndeploymentEventTransformation. However, that would require two input ports.
 * And I have not used the API for multiple input ports.
 * 
 * @author Robert Heinrich
 * @author Reiner Jung
 * 
 */
public class TUndeployment extends AbstractConsumerStage<IUndeploymentRecord> {
	
	/**reference to correspondence interface.*/
	private final ICorrespondence correspondence;

	/**
	 * Most likely the constructor needs an additional field for the PCM access.
	 * But this has to be discussed with Robert.
	 *
	 * @param correspondence
	 */
	public TUndeployment() {
		this.correspondence = null;
	}

	/**
	 * This method is triggered for every undeployment event.
	 */
	@Override
	protected void execute(final IUndeploymentRecord event) {
		// add your transformation here
	}

}
