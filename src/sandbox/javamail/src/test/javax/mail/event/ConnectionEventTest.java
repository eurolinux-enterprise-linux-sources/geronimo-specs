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

package javax.mail.event;
import junit.framework.TestCase;
/**
 * @version $Revision: 1.3 $ $Date: 2004/03/10 10:00:30 $
 */
public class ConnectionEventTest extends TestCase {
    public static class ConnectionListenerTest implements ConnectionListener {
        private int state = 0;
        public void closed(ConnectionEvent event) {
            if (state != 0) {
                fail("Recycled ConnectionListener");
            }
            state = ConnectionEvent.CLOSED;
        }
        public void disconnected(ConnectionEvent event) {
            if (state != 0) {
                fail("Recycled ConnectionListener");
            }
            state = ConnectionEvent.DISCONNECTED;
        }
        public int getState() {
            return state;
        }
        public void opened(ConnectionEvent event) {
            if (state != 0) {
                fail("Recycled ConnectionListener");
            }
            state = ConnectionEvent.OPENED;
        }
    }
    public ConnectionEventTest(String name) {
        super(name);
    }
    private void doEventTests(int type) {
        ConnectionEvent event = new ConnectionEvent(this, type);
        assertEquals(this, event.getSource());
        assertEquals(type, event.getType());
        ConnectionListenerTest listener = new ConnectionListenerTest();
        event.dispatch(listener);
        assertEquals("Unexpcted method dispatched", type, listener.getState());
    }
    public void testEvent() {
        doEventTests(ConnectionEvent.CLOSED);
        doEventTests(ConnectionEvent.OPENED);
        doEventTests(ConnectionEvent.DISCONNECTED);
        try {
            ConnectionEvent event = new ConnectionEvent(this, -12345);
            fail("Expected exception due to invalid type " + event.getType());
        } catch (IllegalArgumentException e) {
        }
    }
}
