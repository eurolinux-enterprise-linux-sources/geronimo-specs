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

import org.apache.geronimo.kernel.Kernel;

/**
 *
 *
 * @version $Revision: 1.1 $ $Date: 2004/07/25 08:22:07 $
 *
 * */
public class StopServer {

    private String kernelName;

    public String getKernelName() {
        return kernelName;
    }

    public void execute() throws Exception {
        Kernel kernel = Kernel.getKernel(getKernelName());
        kernel.shutdown();
    }

    public void setKernelName(String kernelName) {
        this.kernelName = kernelName;
    }
}
