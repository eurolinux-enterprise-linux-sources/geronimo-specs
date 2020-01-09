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

package org.apache.geronimo.j2ee.management;

import org.apache.geronimo.kernel.jmx.MBeanProxyFactory;


/**
 * @version $Revision: 1.4 $ $Date: 2004/05/19 20:53:59 $
 */
public class DomainTest extends Abstract77Test {
    private J2EEDomain domain;

    public void testStandardInterface() throws Exception {
        assertEquals(DOMAIN_NAME.toString(), domain.getobjectName());
        assertObjectNamesEqual(new String[]{SERVER_NAME.toString()}, domain.getservers());
    }

    public void testStandardAttributes() throws Exception {
        assertEquals(DOMAIN_NAME.toString(), mbServer.getAttribute(DOMAIN_NAME, "objectName"));
        assertObjectNamesEqual(new String[]{SERVER_NAME.toString()}, (String[]) mbServer.getAttribute(DOMAIN_NAME, "servers"));
    }

    protected void setUp() throws Exception {
        super.setUp();
        domain = (J2EEDomain) MBeanProxyFactory.getProxy(J2EEDomain.class, mbServer, DOMAIN_NAME);
    }
}
