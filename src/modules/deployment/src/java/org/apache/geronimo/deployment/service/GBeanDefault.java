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

package org.apache.geronimo.deployment.service;

import java.util.Map;

import org.apache.geronimo.gbean.GBeanInfo;

/**
 *
 *
 * @version $Revision: 1.4 $ $Date: 2004/03/10 09:58:49 $
 */
public class GBeanDefault {
    private final String className;
    private final GBeanInfo info;
    private final String objectName;
    private final Map values;
    private final Map endpoints;

    public GBeanDefault(GBeanInfo info, String objectName, Map values, Map endpoints) {
        this.info = info;
        this.objectName = objectName;
        this.values = values;
        this.endpoints = endpoints;
        this.className = null;
    }

    public GBeanDefault(String className, String objectName, Map values, Map endpoints) {
        this.className = className;
        this.objectName = objectName;
        this.values = values;
        this.endpoints = endpoints;
        this.info = null;
    }

    public GBeanInfo getGBeanInfo() {
        return info;
    }

    public String getObjectName() {
        return objectName;
    }

    public Map getValues() {
        return values;
    }

    public String getClassName() {
        return className;
    }

    public Map getEndpoints() {
        return endpoints;
    }

}
