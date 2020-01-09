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

package org.apache.geronimo.console.cli.controller;

import org.apache.geronimo.console.cli.TextController;
import org.apache.geronimo.console.cli.DeploymentContext;
import org.apache.geronimo.console.cli.DConfigBeanConfigurator;

/**
 * Hands over control to {@link org.apache.geronimo.console.cli.DConfigBeanConfigurator} to let the user edit
 * the server-specific deployment information.
 *
 * @version $Revision: 1.3 $ $Date: 2004/03/10 09:58:41 $
 */
public class EditServerSpecificDD extends TextController {
    public EditServerSpecificDD(DeploymentContext context) {
        super(context);
    }

    public void execute() {
        new DConfigBeanConfigurator(context.moduleInfo.getConfigRoot(), context.out, context.in).configure();
    }
}
