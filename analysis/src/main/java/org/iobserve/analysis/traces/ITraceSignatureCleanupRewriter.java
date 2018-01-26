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
package org.iobserve.analysis.traces;

/**
 * Interface for a rewriter on class and operation signatures.
 *
 * @author Reiner Jung
 *
 */
public interface ITraceSignatureCleanupRewriter {

    /**
     * Rewrite the class signature.
     *
     * @param classSignature
     *            old signature
     * @return new signature
     */
    String rewriteClassSignature(String classSignature);

    /**
     * Rewrite the operation signature.
     *
     * @param operationSignature
     *            old signature
     * @return new signature
     */
    String rewriteOperationSignature(String operationSignature);

}