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
package org.apache.geronimo.gbean.jmx;



/**
 * 
 * 
 * @version $Revision: 1.2 $ $Date: 2004/06/03 23:12:54 $
 */
public class VMProxyFactory extends ProxyFactory {
    private final Class proxyType;

    public VMProxyFactory(Class type) {
        if (type.isInterface() == false) {
            throw new IllegalArgumentException("VMProxyFactory can only implement interfaces");
        }
        this.proxyType = type;
    }

    public ProxyMethodInterceptor getMethodInterceptor() {
        return new VMMethodInterceptor(proxyType);
    }

    public Object create(ProxyMethodInterceptor methodInterceptor) {
        return java.lang.reflect.Proxy.newProxyInstance(proxyType.getClassLoader(), new Class[]{proxyType}, (VMMethodInterceptor)methodInterceptor);
    }

    public Object create(ProxyMethodInterceptor methodInterceptor, Class[] types, Object[] arguments) {
        if (types == null || types.length == 0) {
            return create(methodInterceptor);
        } else {
            throw new IllegalArgumentException("VMProxyFactory cannot take constructor args");
        }
    }
}
