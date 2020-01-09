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

package org.apache.geronimo.connector.deployment;

import javax.enterprise.deploy.model.DeployableObject;

import org.apache.geronimo.deployment.plugin.DConfigBeanRootSupport;
import org.apache.geronimo.deployment.plugin.DeploymentConfigurationSupport;

/**
 *
 *
 * @version $Revision: 1.7 $ $Date: 2004/05/30 19:03:36 $
 *
 * */
public class RARConfiguration extends DeploymentConfigurationSupport {

    public RARConfiguration(DeployableObject deployable, DConfigBeanRootSupport dconfigRoot) {
        super(deployable, dconfigRoot);
    }
}
