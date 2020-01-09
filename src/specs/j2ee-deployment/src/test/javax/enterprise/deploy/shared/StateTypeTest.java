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

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//

package javax.enterprise.deploy.shared;

import junit.framework.TestCase;

public class StateTypeTest extends TestCase {
    public void testValues() {
        assertEquals(0, StateType.RUNNING.getValue());
        assertEquals(1, StateType.COMPLETED.getValue());
        assertEquals(2, StateType.FAILED.getValue());
        assertEquals(3, StateType.RELEASED.getValue());
    }

    public void testToString() {
        assertEquals("running", StateType.RUNNING.toString());
        assertEquals("completed", StateType.COMPLETED.toString());
        assertEquals("failed", StateType.FAILED.toString());
        assertEquals("released", StateType.RELEASED.toString());
        // only possible due to package local access
        assertEquals("5", new ActionType(5).toString());
    }

    public void testValueToSmall() {
        try {
            StateType.getStateType(-1);
            fail("Expected AIOOBE");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
        }
    }

    public void testValueToLarge() {
        try {
            StateType.getStateType(5);
            fail("Expected AIOOBE");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
        }
    }
}
