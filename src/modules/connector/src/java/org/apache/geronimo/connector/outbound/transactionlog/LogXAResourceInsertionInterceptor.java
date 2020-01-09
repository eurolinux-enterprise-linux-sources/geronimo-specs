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

package org.apache.geronimo.connector.outbound.transactionlog;

import javax.resource.ResourceException;

import org.apache.geronimo.connector.outbound.ConnectionInfo;
import org.apache.geronimo.connector.outbound.ConnectionInterceptor;
import org.apache.geronimo.connector.outbound.ConnectionReturnAction;
import org.apache.geronimo.connector.outbound.ManagedConnectionInfo;

/**
 * LocalXAResourceInsertionInterceptor.java
 *
 *
 * @version $Revision: 1.2 $ $Date: 2004/06/08 17:38:01 $

 */
public class LogXAResourceInsertionInterceptor
        implements ConnectionInterceptor {

    private final ConnectionInterceptor next;
    private final String name;

    public LogXAResourceInsertionInterceptor(final ConnectionInterceptor next, String name) {
        this.next = next;
        this.name = name;
    }

    public void getConnection(ConnectionInfo connectionInfo) throws ResourceException {
        next.getConnection(connectionInfo);
        ManagedConnectionInfo mci = connectionInfo.getManagedConnectionInfo();
        mci.setXAResource(
                new LogXAResource(
                        mci.getManagedConnection().getLocalTransaction(), name));
    }

    public void returnConnection(
            ConnectionInfo connectionInfo,
            ConnectionReturnAction connectionReturnAction) {
        next.returnConnection(connectionInfo, connectionReturnAction);
    }

}
