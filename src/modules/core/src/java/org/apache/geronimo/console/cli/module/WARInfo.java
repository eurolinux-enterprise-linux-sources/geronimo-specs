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

package org.apache.geronimo.console.cli.module;

import java.io.File;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.spi.DConfigBeanRoot;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import org.apache.geronimo.console.cli.ModuleInfo;
import org.apache.geronimo.console.cli.DeploymentContext;
import org.apache.geronimo.console.cli.controller.InitializeWAR;

/**
 * Holds all the relevent data for a WAR.
 *
 * @version $Revision: 1.3 $ $Date: 2004/03/10 09:58:42 $
 */
public class WARInfo extends ModuleInfo {
    public DDBeanRoot war;
    public DConfigBeanRoot warConfig;

    public WARInfo(DeploymentContext context) {
        super(context);
    }

    public boolean initialize() {
        new InitializeWAR(context).execute();
        return context.moduleInfo != null;
    }

    public String getFileName() {
        return "web.xml";
    }

    public DConfigBeanRoot getConfigRoot() {
        return warConfig;
    }

    public void loadDConfigBean(File source) throws IOException, ConfigurationException {
        BufferedInputStream fin = new BufferedInputStream(new FileInputStream(source));
        DConfigBeanRoot root = context.serverModule.restoreDConfigBean(fin, war);
        fin.close();
        warConfig = root;
    }

    public void saveDConfigBean(File dest) throws IOException, ConfigurationException {
        BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(dest));
        context.serverModule.saveDConfigBean(fout, warConfig);
        fout.close();
    }
}
