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

package org.apache.geronimo.deployment.tools;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.io.InputStream;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.model.DDBeanRoot;

import org.apache.geronimo.deployment.tools.loader.ClientDeployable;
import junit.framework.TestCase;

/**
 * 
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/10 09:58:50 $
 */
public class ClientDeployableTest extends TestCase {
    private ClassLoader classLoader;

    public void testLoadClient() throws Exception {
        URL resource = classLoader.getResource("deployables/app-client1.jar");
        ClientDeployable deployable = new ClientDeployable(resource);
        assertEquals(ModuleType.CAR, deployable.getType());
        Set entrySet = new HashSet(Collections.list(deployable.entries()));
        Set resultSet = new HashSet();
        resultSet.add("META-INF/");
        resultSet.add("META-INF/MANIFEST.MF");
        resultSet.add("META-INF/application-client.xml");
        resultSet.add("Main.java");
        resultSet.add("Main.class");
        assertEquals(resultSet, entrySet);
        InputStream entry = deployable.getEntry("META-INF/application-client.xml");
        assertNotNull(entry);
        entry.close();
        Class main = deployable.getClassFromScope("Main");
        assertEquals("Main", main.getName());

        DDBeanRoot root = deployable.getDDBeanRoot();
        assertNotNull(root);
        assertEquals(ModuleType.CAR, root.getType());
        assertEquals(deployable, root.getDeployableObject());
    }

    protected void setUp() throws Exception {
        classLoader = Thread.currentThread().getContextClassLoader();
    }
}
