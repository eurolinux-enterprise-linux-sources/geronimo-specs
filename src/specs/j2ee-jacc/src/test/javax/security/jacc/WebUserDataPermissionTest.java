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

package javax.security.jacc;

import junit.framework.TestCase;

/**
 *
 * @version $Revision: 1.3 $ $Date: 2004/03/10 09:59:54 $
 */
public class WebUserDataPermissionTest extends TestCase {

    /*
     * Testing WebResourcePermission(java.lang.String, java.lang.String)
     */
    public void testConstructorStringString() {

        WebUserDataPermission permission = new WebUserDataPermission("/foo", "GET,POST:INTEGRAL");

        assertEquals(permission.getName(), "/foo");
        assertEquals(permission.getActions(), "GET,POST:INTEGRAL");
        
        permission = new WebUserDataPermission("/foo", "GET,POST,POST,GET:INTEGRAL");
        assertEquals(permission.getActions(), "GET,POST:INTEGRAL");

        // bad HTTP method
        try {
            permission = new WebUserDataPermission("/foo", "GET,POST,BAR:INTEGRAL");
            fail("Bad HTTP method");
        } catch(IllegalArgumentException iae) {
        }

        // If you have a colon, then you must have a transportType
        try {
            permission = new WebUserDataPermission("/foo", "GET,POST,BAR:");
            fail("Missing transportType");
        } catch(IllegalArgumentException iae) {
        }
    }

    public void testImpliesStringString() {
        // An actions string without a transportType is a shorthand for a 
        // actions string with the value "NONE" as its TransportType
        WebUserDataPermission permissionFooGP = new WebUserDataPermission("/foo", "GET,POST:INTEGRAL");
        WebUserDataPermission permissionFooE = new WebUserDataPermission("/foo", "");
        WebUserDataPermission permissionFooGPN = new WebUserDataPermission("/foo", "GET,POST");
        
        assertTrue(permissionFooE.implies(permissionFooGP));
        assertTrue(permissionFooE.implies(permissionFooGPN));
        assertFalse(permissionFooGP.implies(permissionFooE));
        assertFalse(permissionFooGPN.implies(permissionFooE));

        assertTrue(permissionFooGPN.implies(permissionFooGP));
        assertFalse(permissionFooGP.implies(permissionFooGPN));
    }

    /*
     * Testing WebResourcePermission(String, String[])
     */
    public void testConstructorStringStringArray() {
    }
    
    public void testImpliesStringStringArray() {
    }

    /*
     * Testing WebResourcePermission(HttpServletRequest)
     */
    public void testConstructorHttpServletRequest() {
    }
    
    public void testImpliesHttpServletRequest() {
    }
}

