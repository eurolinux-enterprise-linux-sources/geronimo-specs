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

package org.apache.geronimo.messaging.remotenode;


import org.apache.geronimo.messaging.NodeException;

/**
 * A NodeServer listens for remote nodes and delegates to a
 * RemoteNodeManager their management. 
 * 
 * @version $Revision: 1.2 $ $Date: 2004/07/20 00:15:06 $
 */
public interface NodeServer
{

    /**
     * Start the server.
     * 
     * @throws NodeException If the server can not be started.
     * @exception IllegalStateException Indicates that no RemoteNodeManger has
     * been set.
     */
    public void start() throws NodeException, IllegalStateException;

    /**
     * Stop the server.
     */
    public void stop();

    /**
     * Sets the RemoteNodeManager in charge of managing the remote nodes, which
     * have joined this server.
     * <BR>
     * A NodeServer must notify this RemoteNodeManager when a new connection
     * abstracting a remote note has joined it.
     * 
     * @param aManager Manager of RemoteNode.
     */
    public void setRemoteNodeManager(RemoteNodeManager aManager);
    
}