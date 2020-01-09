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

package org.apache.geronimo.gbean;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @version $Revision: 1.5 $ $Date: 2004/06/02 05:33:03 $
 */
public class GBeanInfoTest extends TestCase {
    private static final String CONSTRUCTOR_ARG_0 = "ConstructorArg-0";
    private static final String CONSTRUCTOR_ARG_1 = "ConstructorArg-1";

    public void testGetGBeanInfo() {
        // 1. Test GBean that exists
        GBeanInfo gbeanInfo = GBeanInfo.getGBeanInfo(MockGBean.class.getName(), MockGBean.class.getClassLoader());
        assertNotNull(gbeanInfo);

        // 2. Test GBean that doesn't exist
        try {
            GBeanInfo.getGBeanInfo("ClassThatDoesNotExist", this.getClass().getClassLoader());
            fail("InvalidConfigurationException expected");
        } catch (InvalidConfigurationException expected) {
        }

        // 3. Test GBean that exist, but doesn't declare a getGBeanInfo()
        // method
        try {
            GBeanInfo.getGBeanInfo(String.class.getName(), this.getClass().getClassLoader());
            fail("InvalidConfigurationException expected");
        } catch (InvalidConfigurationException expected) {
        }
    }

    /*
     * void GBeanInfo(String, Set, GConstructorInfo, Set, Set, Set)
     */
    public void testGBeanInfoStringSetGConstructorInfoSetSetSet() {
        GBeanInfo gbeanInfo = new GBeanInfo(null, null, null, null, null, null);
        assertNotNull(gbeanInfo);
    }

    /*
     * void GBeanInfo(String, String, Set, GConstructorInfo, Set, Set, Set)
     */
    public void testGBeanInfoStringStringSetGConstructorInfoSetSetSet() {
        GBeanInfo gbeanInfo = new GBeanInfo(null, null, null, null, null, null, null);
        assertNotNull(gbeanInfo);
    }

    public void testGetName() {
        assertEquals(MockGBean.class.getName(), gbeanInfo.getName());
    }

    public void testGetClassName() {
        assertEquals(MockGBean.class.getName(), gbeanInfo.getClassName());
    }

    public void testGetAttributeSet() {
        Set attrSet = gbeanInfo.getAttributes();
        assertEquals(2, attrSet.size());
        assertTrue(attrSet.contains(persistentAttrInfo));
        assertTrue(attrSet.contains(nonPersistentAttrInfo));
    }

    public void testGetPersistentAttributes() {
        List attrList = gbeanInfo.getPersistentAttributes();
        assertEquals(1, attrList.size());
        assertEquals(persistentAttrInfo, attrList.get(0));
    }

    public void testGetConstructor() {
        GConstructorInfo gctorInfo = gbeanInfo.getConstructor();
        List attrNameList = gctorInfo.getAttributeNames();
        assertEquals(2, attrNameList.size());
        assertEquals(CONSTRUCTOR_ARG_0, attrNameList.get(0));
        assertEquals(CONSTRUCTOR_ARG_1, attrNameList.get(1));
    }

    public void testGetOperationsSet() {
        Set gbeanOpSet = gbeanInfo.getOperations();
        assertEquals(1, gbeanOpSet.size());
        assertTrue(gbeanOpSet.contains(opInfo));
    }

    public void testGetNotificationsSet() {
        Set gbeanNotificationSet = gbeanInfo.getNotifications();
        assertEquals(1, gbeanNotificationSet.size());
        assertTrue(gbeanNotificationSet.contains(notificationInfo));
    }

    public void testGetReferencesSet() {
        Set gbeanRefSet = gbeanInfo.getReferences();
        assertEquals(1, gbeanRefSet.size());
        assertTrue(gbeanRefSet.contains(refInfo));
    }

    public void testToString() {
        assertNotNull(gbeanInfo.toString());
        assertEquals(gbeanInfo.toString(), MockGBean.getGBeanInfo().toString());
    }

    GBeanInfo gbeanInfo;

    final static String nonPersistentAttrName = "nonPersistentAttribute";

    final static GAttributeInfo nonPersistentAttrInfo = new GAttributeInfo(nonPersistentAttrName, String.class.getName(), false);

    final static String persistentAttrName = "persistentAttribute";

    final static GAttributeInfo persistentAttrInfo = new GAttributeInfo(persistentAttrName, String.class.getName(), true);

    final static GOperationInfo opInfo = new GOperationInfo("operation");

    final static GNotificationInfo notificationInfo = new GNotificationInfo("notification", Collections.singleton(null));

    final static GReferenceInfo refInfo = new GReferenceInfo("reference", String.class);

    public void setUp() {
        gbeanInfo = MockGBean.getGBeanInfo();
    }

    protected void tearDown() throws Exception {
        gbeanInfo = null;
    }

    public static final class MockGBean {
        public static final GBeanInfo GBEAN_INFO;

        static {
            GBeanInfoFactory infoFactory = new GBeanInfoFactory(MockGBean.class);

            infoFactory.addAttribute(nonPersistentAttrInfo);
            infoFactory.addAttribute(persistentAttrInfo);

            infoFactory.addOperation(opInfo);

            infoFactory.addNotification(notificationInfo);

            infoFactory.addReference(refInfo);

            infoFactory.setConstructor(new String[]{CONSTRUCTOR_ARG_0, CONSTRUCTOR_ARG_1});

            GBEAN_INFO = infoFactory.getBeanInfo();
        }

        public static GBeanInfo getGBeanInfo() {
            return GBEAN_INFO;
        }
    }
}
