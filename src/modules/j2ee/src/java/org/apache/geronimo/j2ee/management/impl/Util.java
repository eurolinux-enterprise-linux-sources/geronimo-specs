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

package org.apache.geronimo.j2ee.management.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.geronimo.kernel.Kernel;

/**
 * @version $Revision: 1.5 $ $Date: 2004/06/04 22:31:56 $
 */
public class Util {
    public static String[] getObjectNames(Kernel kernel, Object parentName, String[] j2eeTypes) throws MalformedObjectNameException {
        List objectNames = new LinkedList();
        for (int i = 0; i < j2eeTypes.length; i++) {
            String j2eeType = j2eeTypes[i];
            objectNames.addAll(kernel.listGBeans(new ObjectName(parentName + "j2eeType=" + j2eeType + ",*")));
        }
        String[] names = new String[objectNames.size()];
        Iterator iterator = objectNames.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            names[i] = iterator.next().toString();
        }
        return names;
    }
}
