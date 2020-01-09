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

package org.apache.geronimo.network.protocol.control;

import org.apache.geronimo.network.protocol.AbstractProtocol;


/**
 * @version $Revision: 1.2 $ $Date: 2004/03/10 09:59:14 $
 */
public abstract class AbstractControlProtocol extends AbstractProtocol {

    /**
     * reserved commands
     */
    final static byte PASSTHROUGH = (byte) 0x00;
    final static byte BOOT_REQUEST = (byte) 0x01;
    final static byte BOOT_RESPONSE = (byte) 0x02;
    final static byte BOOT_SUCCESS = (byte) 0x03;
    final static byte SHUTDOWN_REQ = (byte) 0x04;
    final static byte SHUTDOWN_ACK = (byte) 0x05;
    final static byte NOBOOT = (byte) 0x06;
    final static byte RESERVED = (byte) 0xff;
}
