/**
 *
 * Copyright 2004 The Apache Software Foundation
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

package org.apache.geronimo.connector;

import java.io.Serializable;

import org.apache.geronimo.gbean.GBeanInfo;

/**
 * Holds info needed to create an ActivationSpecWrapper gbean for an mdb.
 * This can be extended to include values from the ResourceAdapter configuration for
 * duplicated properties.
 *
 * @version $Revision: 1.1 $ $Date: 2004/06/25 21:33:26 $
 *
 * */
public class ActivationSpecInfo implements Serializable {

    private final Class activationSpecClass;
    private final GBeanInfo activationSpecGBeanInfo;

    public ActivationSpecInfo(Class activationSpecClass, GBeanInfo activationSpecGBeanInfo) {
        this.activationSpecClass = activationSpecClass;
        this.activationSpecGBeanInfo = activationSpecGBeanInfo;
    }

    public Class getActivationSpecClass() {
        return activationSpecClass;
    }

    public GBeanInfo getActivationSpecGBeanInfo() {
        return activationSpecGBeanInfo;
    }

}
