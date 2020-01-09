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
package org.apache.geronimo.system;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.geronimo.gbean.GBeanInfo;
import org.apache.geronimo.gbean.GBeanInfoFactory;
import org.apache.geronimo.gbean.GBeanLifecycle;
import org.apache.geronimo.gbean.WaitingException;

/**
 * Thin GBean wrapper around the RMI Registry.
 *
 * @version $Revision: 1.5 $ $Date: 2004/07/12 06:07:52 $
 */
public class RMIRegistryService implements GBeanLifecycle {
    private static final Log log = LogFactory.getLog(RMIRegistryService.class);
    private int port = Registry.REGISTRY_PORT;
    private Registry registry;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void doStart() throws WaitingException, Exception {
        registry = LocateRegistry.createRegistry(port);
        log.info("Started RMI Registry on port " + port);
    }

    public void doStop() throws WaitingException, Exception {
        UnicastRemoteObject.unexportObject(registry, true);
        log.info("Stopped RMI Registry");
    }

    public void doFail() {
        try {
            doStop();
        } catch (Exception e) {
            log.warn("RMI Registry failed");
        }
    }

    public static final GBeanInfo GBEAN_INFO;

    static {
        GBeanInfoFactory infoFactory = new GBeanInfoFactory(RMIRegistryService.class);
        infoFactory.addAttribute("port", int.class, true);
        GBEAN_INFO = infoFactory.getBeanInfo();
    }

    public static GBeanInfo getGBeanInfo() {
        return GBEAN_INFO;
    }
}
