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

import EDU.oswego.cs.dl.util.concurrent.Latch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.geronimo.network.protocol.DownPacket;
import org.apache.geronimo.network.protocol.ProtocolException;
import org.apache.geronimo.network.protocol.UpPacket;


/**
 * @version $Revision: 1.8 $ $Date: 2004/08/01 13:03:50 $
 */
public class ControlClientProtocol extends AbstractControlProtocol {

    final static private Log log = LogFactory.getLog(ControlClientProtocol.class);

    private ControlClientListener listener;
    private long timeout;


    public ControlClientListener getListener() {
        return listener;
    }

    public void setListener(ControlClientListener listener) {
        this.listener = listener;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setup() throws ProtocolException {
        log.trace("Starting");

        getDownProtocol().sendDown(new BootRequestDownPacket()); //todo: this is probably dangerous, put in thread pool
    }

    public void drain() throws ProtocolException {
        log.trace("Stopping");
        if (state == RUN) {
            getDownProtocol().sendDown(new ShutdownRequestDownPacket());
            getDownProtocol().flush();
        }
    }

    public void teardown() throws ProtocolException {
    }

    public void sendUp(UpPacket packet) throws ProtocolException {
        state.sendUp(packet);
    }

    public void sendDown(DownPacket packet) throws ProtocolException {
        state.sendDown(packet);
    }

    public void flush() throws ProtocolException {
        getDownProtocol().flush();
    }

    private final State START = new State(this) {
        Latch startupLatch = new Latch();

        public void sendUp(UpPacket packet) throws ProtocolException {
            UpPacket p = ControlPacketReader.getInstance().read(packet.getBuffer());
            if (p instanceof BootResponseUpPacket) {
                try {
                    log.trace("BOOT RESPONSE");
                    listener.serveUp(((BootResponseUpPacket) p).getMenu());
                    getDownProtocol().sendDown(new BootSuccessDownPacket());
                    log.trace("RELEASING " + startupLatch);
                    state = RUN;
                    startupLatch.release();
                    log.trace("RELEASED " + startupLatch);
                } catch (ControlException e) {
                    throw new ProtocolException(e);
                }
            } else if (p instanceof NoBootUpPacket) {
                log.trace("NO BOOT");
                listener.shutdown();
            }
        }

        public void sendDown(DownPacket packet) throws ProtocolException {
            try {
                log.trace("AQUIRING " + startupLatch);
                if (!startupLatch.attempt(timeout)) throw new ProtocolException("Send timeout");
                log.trace("AQUIRED " + startupLatch);

                PassthroughDownPacket passthtough = new PassthroughDownPacket();
                passthtough.setBuffers(packet.getBuffers());

                getDownProtocol().sendDown(passthtough);
            } catch (InterruptedException e) {
                throw new ProtocolException(e);
            }
        }
    };

    private final State RUN = new State(this) {

        public void sendUp(UpPacket packet) throws ProtocolException {
            UpPacket p = ControlPacketReader.getInstance().read(packet.getBuffer());
            if (p instanceof PassthroughUpPacket) {
                log.trace("PASSTHROUGH");
                getUpProtocol().sendUp(packet);
            } else if (p instanceof ShutdownRequestUpPacket) {
                log.trace("SHUTDOWN_REQ");
                getDownProtocol().sendDown(new ShutdownAcknowledgeDownPacket());
                listener.shutdown();
                state = START;
            }
        }

        public void sendDown(DownPacket packet) throws ProtocolException {
            PassthroughDownPacket passthtough = new PassthroughDownPacket();
            passthtough.setBuffers(packet.getBuffers());

            getDownProtocol().sendDown(passthtough);
        }
    };

    private volatile State state = START;
}
