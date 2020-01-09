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
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.DeploymentConfiguration;

import org.apache.geronimo.connector.deployment.dconfigbean.ResourceAdapterDConfigRoot;
import org.apache.geronimo.connector.deployment.dconfigbean.ResourceAdapter_1_0DConfigRoot;
import org.apache.geronimo.deployment.ModuleConfigurer;
import org.apache.geronimo.gbean.GBeanInfo;
import org.apache.geronimo.gbean.GBeanInfoFactory;

/**
 *
 *
 * @version $Revision: 1.6 $ $Date: 2004/05/30 19:03:36 $
 */
public class RARConfigurer implements ModuleConfigurer {

    public DeploymentConfiguration createConfiguration(DeployableObject deployable) {
        if (ModuleType.RAR.equals(deployable.getType())) {
            if (deployable.getDDBeanRoot().getDDBeanRootVersion().equals("1.5")) {
                return new RARConfiguration(deployable, new ResourceAdapterDConfigRoot(deployable.getDDBeanRoot()));
            }
            String[] specVersion = deployable.getDDBeanRoot().getText("connector/spec-version");
            if (specVersion.length > 0 && "1.0".equals(specVersion[0])) {
                return new RARConfiguration(deployable, new ResourceAdapter_1_0DConfigRoot(deployable.getDDBeanRoot()));
            }
            throw new IllegalArgumentException("Unknown resource adapter version: " + deployable.getDDBeanRoot().getDDBeanRootVersion());
        } else {
            return null;
        }
    }

    public static final GBeanInfo GBEAN_INFO;

    static {
        GBeanInfoFactory infoFactory = new GBeanInfoFactory(RARConfigurer.class);
        infoFactory.addInterface(ModuleConfigurer.class);
        GBEAN_INFO = infoFactory.getBeanInfo();
    }

    public static GBeanInfo getGBeanInfo() {
        return GBEAN_INFO;
    }
}
