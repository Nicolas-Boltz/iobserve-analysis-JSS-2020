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
package org.iobserve.analysis.correspondence;

/**
 * Object to encapsulate some data for the correspondence of PCMElement and code
 * artifacts.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public final class Correspondent {

	/**entity name.*/
	private final String pcmEntityName;
	/**entity id.*/
	private final String pcmEntityId;
	/**operation name.*/
	private final String pcmOperationName;
	/**operation id.*/
	private final String pcmOperationId;

	/**
	 * Package protected constructor. Correspondent object should be created by
	 * the {@link CorrespondentFactory}. This object is immutable.
	 * 
	 * @param thePcmEntityName
	 *            entity name.
	 * @param thePcmEntityId
	 *            entity id.
	 * @param thePcmOperationName
	 *            operation name.
	 * @param thePcmOperationId
	 *            operation id.
	 */
	Correspondent(final String thePcmEntityName, final String thePcmEntityId,
			final String thePcmOperationName, final String thePcmOperationId) {
		this.pcmEntityName = thePcmEntityName;
		this.pcmEntityId = thePcmEntityId;
		this.pcmOperationName = thePcmOperationName;
		this.pcmOperationId = thePcmOperationId;
	}

	/**
	 * @return entity name
	 */
	public String getPcmEntityName() {
		return this.pcmEntityName;
	}

	/**
	 * @return entity id
	 */
	public String getPcmEntityId() {
		return this.pcmEntityId;
	}

	/**
	 * @return operation name
	 */
	public String getPcmOperationName() {
		return this.pcmOperationName;
	}

	/**
	 * @return operation id
	 */
	public String getPcmOperationId() {
		return this.pcmOperationId;
	}
}
