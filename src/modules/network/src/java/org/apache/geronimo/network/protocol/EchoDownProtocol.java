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
package org.apache.geronimo.network.protocol;

import java.util.Collections;


/**
 * @version $Revision: 1.2 $ $Date: 2004/08/01 13:03:43 $
 */
public class EchoDownProtocol extends AbstractProtocol {

    public void setup() throws ProtocolException {
    }

    public void drain() throws ProtocolException {
    }

    public void teardown() throws ProtocolException {
    }

    public void sendUp(UpPacket packet) throws ProtocolException {
        PlainDownPacket dnPacket = new PlainDownPacket();

        dnPacket.setBuffers(Collections.singleton(packet.getBuffer()));

        getDownProtocol().sendDown(dnPacket);
    }

    public void sendDown(DownPacket packet) throws ProtocolException {
        getDownProtocol().sendDown(packet);
    }

    public void flush() throws ProtocolException {
        getDownProtocol().flush();
    }
}
