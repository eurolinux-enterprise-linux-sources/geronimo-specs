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

package org.apache.geronimo.jetty.deployment;

import org.apache.geronimo.naming.deployment.RefAdapter;
import org.apache.geronimo.xbeans.geronimo.jetty.JettyRemoteRefType;
import org.apache.xmlbeans.XmlObject;

/**
 *
 *
 * @version $Revision: 1.1 $ $Date: 2004/03/09 18:03:52 $
 *
 * */
public class JettyRefAdapter implements RefAdapter {

    private JettyRemoteRefType ref;

    public JettyRefAdapter(JettyRemoteRefType ref) {
        this.ref = ref;
    }

    public XmlObject getXmlObject() {
        return ref;
    }

    public void setXmlObject(XmlObject xmlObject) {
        ref = (JettyRemoteRefType)xmlObject;
    }

    public String getRefName() {
        return ref.getRefName();
    }

    public void setRefName(String name) {
        ref.setRefName(name);
    }

    public String getServerName() {
        return ref.getServer();
    }

    public void setServerName(String serverName) {
        ref.setServer(serverName);
    }

    public String getKernelName() {
        return ref.getKernelName();
    }

    public void setKernelName(String kernelName) {
        ref.setKernelName(kernelName);
    }

    public String getTargetName() {
        return ref.getTargetName();
    }

    public void setTargetName(String targetName) {
        ref.setTargetName(targetName);
    }

    public String getExternalUri() {
        return ref.getExternalUri();
    }

    public void setExternalUri(String externalURI) {
        ref.setExternalUri(externalURI);
    }

}
