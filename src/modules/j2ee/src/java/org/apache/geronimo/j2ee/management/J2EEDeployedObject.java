/**
 *
 * Copyright 2003-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.geronimo.j2ee.management;

/**
 * 
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/10 09:58:52 $
 */
public interface J2EEDeployedObject extends J2EEManagedObject {
    /**
     * The deploymentDescriptor string must contain the original XML deployment
     * descriptor that was created for this module during the deployment process.
     * @see "JSR77.3.5.0.1"
     * @return this module's deployment descriptor
     */
    String getdeploymentDescriptor();

    /**
     * The J2EE server the application or module is deployed on.
     * @see "JSR77.3.5.0.2"
     * @return the server this module is deployed on
     */
    String getserver();
}
