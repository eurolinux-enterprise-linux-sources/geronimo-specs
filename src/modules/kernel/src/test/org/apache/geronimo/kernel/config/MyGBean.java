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

package org.apache.geronimo.kernel.config;

import org.apache.geronimo.gbean.GBeanInfo;
import org.apache.geronimo.gbean.GBeanInfoFactory;

/**
 * 
 * 
 * @version $Revision: 1.4 $ $Date: 2004/06/02 05:33:04 $
 */
public class MyGBean {
    public void main(String[] args) {
        System.out.println("Hello World");
    }

    public static final GBeanInfo GBEAN_INFO;
    static {
        GBeanInfoFactory infoFactory = new GBeanInfoFactory(MyGBean.class);
        infoFactory.addOperation("main", new Class[] {String[].class});
        GBEAN_INFO = infoFactory.getBeanInfo();
    }
}
