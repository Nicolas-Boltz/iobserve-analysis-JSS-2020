/***************************************************************************
 * Copyright 2016 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.userbehavior.data;

/**
 * Interface that state the elements of a call sequence.
 *
 * @author David Peter, Robert Heinrich
 */
public interface ISequenceElement {

    /**
     * @return returns the count
     */
    public int getAbsoluteCount();

    /**
     * @param absoluteCount
     *            sets the count.
     */
    public void setAbsoluteCount(int absoluteCount);

    /**
     *
     * @return returns the class signature for the element.
     */
    public String getClassSignature();

    /**
     *
     * @return returns the operation signature for the element.
     */
    public String getOperationSignature();

}