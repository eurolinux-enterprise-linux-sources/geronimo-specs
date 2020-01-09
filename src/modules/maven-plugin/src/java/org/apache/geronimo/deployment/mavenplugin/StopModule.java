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

package org.apache.geronimo.deployment.mavenplugin;

import java.util.List;
import java.util.ArrayList;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.status.ProgressObject;

/**
 *
 *
 * @version $Revision: 1.1 $ $Date: 2004/07/25 08:22:07 $
 *
 * */
public class StopModule extends AbstractModuleCommand {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void execute() throws Exception {
        DeploymentManager manager = getDeploymentManager();

        Target[] targets = manager.getTargets();
        TargetModuleID moduleIds[] = manager.getRunningModules(null, targets);
        List toStop = new ArrayList(moduleIds.length);
        for (int i = 0; i < moduleIds.length; i++) {
            TargetModuleID moduleId = moduleIds[i];
            if (getId().equals(moduleId.getModuleID())) {
                toStop.add(moduleId);
            }
        }
        if (toStop.size() == 0) {
            System.out.println("Module is not running or not deployed: " + getId());
            return;
        }
        moduleIds = (TargetModuleID[]) toStop.toArray(new TargetModuleID[toStop.size()]);
        ProgressObject progress = manager.stop(moduleIds);
        DeploymentClient.waitFor(progress);
    }

}
