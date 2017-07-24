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
package org.iobserve.analysis.cdoruserbehavior.filter.models;

/**
 * Represents a additional Information of a Call
 *
 * @author Christoph Dornieden
 *
 */

public class CallInformation {
    private String informationSignature;
    private double informationCode;

    /*
     *
     * base constructor
     */
    public CallInformation() {
        // Empty
    }

    /**
     * constructor
     *
     * @param informationSignature
     *            informationSignature
     * @param informationCode
     *            informationCode
     */
    public CallInformation(final String informationSignature, final double informationCode) {
        this.informationSignature = informationSignature;
        this.informationCode = informationCode;
    }

    /**
     * getter
     *
     * @return informationSignature
     */
    public String getInformationSignature() {
        return this.informationSignature;
    }

    /**
     * setter
     *
     * @param signature
     *            information signature
     */
    public void setInformationSignature(final String signature) {
        this.informationSignature = signature;
    }

    /**
     * getter
     *
     * @return informationCode
     */
    public double getInformationCode() {
        return this.informationCode;
    }

    /**
     * setter
     *
     * @param code
     *            code
     */
    public void setInformationCode(final double code) {
        this.informationCode = code;

    }

}