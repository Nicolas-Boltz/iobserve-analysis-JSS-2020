/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
package org.iobserve.common.record.junit;
		
import org.junit.Assert;
import org.junit.Test;

import org.iobserve.common.record.ContainerDeallocationEvent;

import kieker.test.common.junit.AbstractGeneratedKiekerTest;
		
/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 * 
 * @author Generic Kieker
 * 
 * @since 1.13
 */
public class TestGeneratedContainerDeallocationEvent extends AbstractGeneratedKiekerTest {

	public TestGeneratedContainerDeallocationEvent() {
		// empty default constructor
	}

	/**
	 * Tests {@link ContainerDeallocationEvent#TestContainerDeallocationEvent(string)}.
	 */
	@Test
	public void testToArray() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			ContainerDeallocationEvent record = new ContainerDeallocationEvent(STRING_VALUES.get(i % STRING_VALUES.size()));
			
			// check values
			Assert.assertEquals("ContainerDeallocationEvent.url values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getUrl());
			
			Object[] values = record.toArray();
			
			Assert.assertNotNull("Record array serialization failed. No values array returned.", values);
			Assert.assertEquals("Record array size does not match expected number of properties 1.", 1, values.length);
			
			// check all object values exist
			Assert.assertNotNull("Array value [0] of type String must be not null.", values[0]); 
			
			// check all types
			Assert.assertTrue("Type of array value [0] " + values[0].getClass().getCanonicalName() + " does not match the desired type String", values[0] instanceof String);
								
			// check all object values 
			Assert.assertEquals("Array value [0] " + values[0] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), values[0]
			);
		}
	}
	
	/**
	 * Tests {@link ContainerDeallocationEvent#TestContainerDeallocationEvent(string)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			ContainerDeallocationEvent record = new ContainerDeallocationEvent(STRING_VALUES.get(i % STRING_VALUES.size()));
			
			// check values
			Assert.assertEquals("ContainerDeallocationEvent.url values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getUrl());
		}
	}
	
	/**
	 * Tests {@link ContainerDeallocationEvent#TestContainerDeallocationEvent(string)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			ContainerDeallocationEvent record = new ContainerDeallocationEvent(STRING_VALUES.get(i % STRING_VALUES.size()));
			
			// check values
			Assert.assertEquals("ContainerDeallocationEvent.url values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getUrl());
		}
	}
	
	@Test
	public void testEquality() {
		int i = 0;
		ContainerDeallocationEvent oneRecord = new ContainerDeallocationEvent(STRING_VALUES.get(i % STRING_VALUES.size()));
		i = 0;
		ContainerDeallocationEvent copiedRecord = new ContainerDeallocationEvent(STRING_VALUES.get(i % STRING_VALUES.size()));
		
		Assert.assertEquals(oneRecord, copiedRecord);
	}	
	
	@Test
	public void testUnequality() {
		int i = 0;
		ContainerDeallocationEvent oneRecord = new ContainerDeallocationEvent(STRING_VALUES.get(i % STRING_VALUES.size()));
		i = 2;
		ContainerDeallocationEvent anotherRecord = new ContainerDeallocationEvent(STRING_VALUES.get(i % STRING_VALUES.size()));
		
		Assert.assertNotEquals(oneRecord, anotherRecord);
	}
}
