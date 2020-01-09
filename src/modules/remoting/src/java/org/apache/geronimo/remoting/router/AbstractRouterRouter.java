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

package org.apache.geronimo.remoting.router;

import java.net.URI;

import EDU.oswego.cs.dl.util.concurrent.Latch;
import EDU.oswego.cs.dl.util.concurrent.Sync;
import EDU.oswego.cs.dl.util.concurrent.TimeoutSync;
import org.apache.geronimo.gbean.GBeanLifecycle;
import org.apache.geronimo.remoting.transport.Msg;
import org.apache.geronimo.remoting.transport.TransportException;

/**
 * @version $Revision: 1.7 $ $Date: 2004/06/05 07:53:22 $
 */
public abstract class AbstractRouterRouter implements GBeanLifecycle, Router {
    private long stoppedRoutingTimeout = 1000 * 60; // 1 min.

    /**
     * Allows us to pause invocations when in the stopped state.
     */
    private Sync routerLock = createNewRouterLock();

    public long getStoppedRoutingTimeout() {
        return stoppedRoutingTimeout;
    }

    public void setStoppedRoutingTimeout(long stoppedRoutingTimeout) {
        this.stoppedRoutingTimeout = stoppedRoutingTimeout;
    }

    private Sync createNewRouterLock() {
        Latch lock = new Latch();
        return new TimeoutSync(lock, stoppedRoutingTimeout);
    }

    public Msg sendRequest(URI to, Msg msg) throws TransportException {
        try {
            routerLock.acquire();
            Router next = lookupRouterFrom(to);
            if (next == null) {
                throw new TransportException("No route is available to: " + to);
            }

            return next.sendRequest(to, msg);

        } catch (Throwable e) {
            e.printStackTrace();
            throw new TransportException(e.getMessage());
        }
    }

    public void sendDatagram(URI to, Msg msg) throws TransportException {
        try {
            routerLock.acquire();
            Router next = lookupRouterFrom(to);
            next.sendDatagram(to, msg);
        } catch (Throwable e) {
            throw new TransportException(e.getMessage());
        }
    }

    abstract protected Router lookupRouterFrom(URI to);

    public void doStart() {
        routerLock.release();
    }

    public void doStop() {
        routerLock = createNewRouterLock();
    }

    public void doFail() {
        // @todo do your best to clean up after a failure
    }

}
