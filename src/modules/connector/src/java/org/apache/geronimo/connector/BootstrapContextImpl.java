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

package org.apache.geronimo.connector;

import java.util.Timer;

import javax.resource.spi.UnavailableException;
import javax.resource.spi.XATerminator;
import javax.resource.spi.work.WorkManager;

import org.apache.geronimo.connector.work.GeronimoWorkManager;

/**
 * GBean BootstrapContext implementation that refers to externally configured WorkManager
 * and XATerminator gbeans.
 *
 * @version $Revision: 1.1 $ $Date: 2004/07/11 21:55:33 $
 */
public class BootstrapContextImpl implements javax.resource.spi.BootstrapContext {
    private final GeronimoWorkManager workManager;

    /**
     * Normal constructor for use as a GBean.
     * @param workManager
     */
    public BootstrapContextImpl(final GeronimoWorkManager workManager) {
        this.workManager = workManager;
    }


    /**
     * @see javax.resource.spi.BootstrapContext#getWorkManager()
     */
    public WorkManager getWorkManager() {
        return workManager;
    }

    /**
     * @see javax.resource.spi.BootstrapContext#getXATerminator()
     */
    public XATerminator getXATerminator() {
        return workManager.getXATerminator();
    }

    /**
     * @see javax.resource.spi.BootstrapContext#createTimer()
     */
    public Timer createTimer() throws UnavailableException {
        return new Timer();
    }

//    public static final GBeanInfo GBEAN_INFO;
//
//    static {
//        GBeanInfoFactory infoFactory = new GBeanInfoFactory(BootstrapContext.class);
//          //adding interface does not work, creates attributes for references???
////        infoFactory.addInterface(javax.resource.spi.BootstrapContext.class);
//
//        infoFactory.addOperation("createTimer");
//        infoFactory.addOperation("getWorkManager");
//        infoFactory.addOperation("getXATerminator");
//
//        infoFactory.addReference("WorkManager", WorkManager.class);
//        infoFactory.addReference("XATerminator", XATerminator.class);
//
//        infoFactory.setConstructor(new String[]{"WorkManager", "XATerminator"});
//
//        GBEAN_INFO = infoFactory.getBeanInfo();
//    }
//
//    public static GBeanInfo getGBeanInfo() {
//        return GBEAN_INFO;
//    }

}
