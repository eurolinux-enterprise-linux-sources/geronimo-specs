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

package org.apache.geronimo.connector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.management.ObjectName;

import junit.framework.TestCase;
import org.apache.geronimo.connector.mock.MockAdminObject;
import org.apache.geronimo.connector.mock.MockAdminObjectImpl;
import org.apache.geronimo.gbean.jmx.GBeanMBean;
import org.apache.geronimo.kernel.Kernel;
import org.apache.geronimo.naming.deployment.RefAdapter;
import org.apache.geronimo.naming.java.ComponentContextBuilder;
import org.apache.geronimo.naming.java.ReadOnlyContext;
import org.apache.geronimo.naming.jmx.JMXReferenceFactory;
import org.apache.xmlbeans.XmlObject;

/**
 * @version $Revision: 1.4 $ $Date: 2004/07/12 06:07:52 $
 */
public class AdminObjectWrapperTest extends TestCase {

    private Kernel kernel;
    private ObjectName selfName;
    private static final String KERNEL_NAME = "testKernel";
    private static final String TARGET_NAME = "testAOName";

    public void testProxy() throws Exception {
        Object proxy = kernel.invoke(selfName, "getProxy");
        assertNotNull(proxy);
        assertTrue(proxy instanceof MockAdminObject);
        MockAdminObject mockAdminObject = ((MockAdminObject) proxy).getSomething();
        assertNotNull(mockAdminObject);
        kernel.stopGBean(selfName);
        try {
            ((MockAdminObject) proxy).getSomething();
            fail();
        } catch (IllegalStateException ise) {
        }
        kernel.startGBean(selfName);
        ((MockAdminObject) proxy).getSomething();
    }

    public void testSerialization() throws Exception {
        MockAdminObject proxy = (MockAdminObject) kernel.invoke(selfName, "getProxy");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(proxy);
        oos.flush();
        byte[] bytes = baos.toByteArray();
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Object proxy2 = ois.readObject();
        assertNotNull(proxy2);
        assertTrue(proxy instanceof MockAdminObject);
        MockAdminObject mockAdminObject = proxy.getSomething();
        assertNotNull(mockAdminObject);
        kernel.stopGBean(selfName);
        ObjectInputStream ois2 = new ObjectInputStream(new ByteArrayInputStream(bytes));
        MockAdminObject proxy3 = (MockAdminObject) ois2.readObject();
        try {
            proxy3.getSomething();
            fail();
        } catch (IllegalStateException ise) {
        }
        kernel.startGBean(selfName);
        proxy3.getSomething();

    }


    public void testLocalLookup() throws Exception {
        JMXReferenceFactory referenceFactory = new JMXReferenceFactory();
        ComponentContextBuilder builder = new ComponentContextBuilder(referenceFactory);
        builder.addResourceEnvRef("resourceenvref", MockAdminObject.class, new RefAdapter() {
            public XmlObject getXmlObject() {
                return null;
            }

            public void setXmlObject(XmlObject xmlObject) {
            }

            public String getRefName() {
                return "resourceenvref";
            }

            public void setRefName(String name) {
            }

            public String getServerName() {
                return null;
            }

            public void setServerName(String serverName) {
            }

            public String getKernelName() {
                return KERNEL_NAME;
            }

            public void setKernelName(String kernelName) {
            }

            public String getTargetName() {
                return TARGET_NAME;
            }

            public void setTargetName(String targetName) {
            }

            public String getExternalUri() {
                return null;
            }

            public void setExternalUri(String externalURI) {
            }

        });
        ReadOnlyContext roc = builder.getContext();
        Object o = roc.lookup("env/resourceenvref");
        assertNotNull(o);
        assertTrue(o instanceof MockAdminObject);
    }

    protected void setUp() throws Exception {
        kernel = new Kernel(KERNEL_NAME, "test.domain");
        kernel.boot();

        selfName = ObjectName.getInstance(JMXReferenceFactory.BASE_ADMIN_OBJECT_NAME + TARGET_NAME);

        GBeanMBean aow = new GBeanMBean(AdminObjectWrapper.getGBeanInfo());
        aow.setAttribute("adminObjectInterface", MockAdminObject.class);
        aow.setAttribute("adminObjectClass", MockAdminObjectImpl.class);
        kernel.loadGBean(selfName, aow);

        kernel.startGBean(selfName);
    }

    protected void tearDown() throws Exception {
        kernel.stopGBean(selfName);
        kernel.shutdown();
    }
}
