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

package org.apache.geronimo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.geronimo.core.service.Invocation;
import org.apache.geronimo.core.service.InvocationResult;
import org.apache.geronimo.core.service.Interceptor;

/**
 * A local container that is a proxy for some other "real" container.
 * This container is itself fairly unintelligent; you need to add some
 * interceptors to get the desired behavior (i.e. contacting the real
 * server on every request).  For example, see
 * {@link org.apache.geronimo.remoting.jmx.RemoteMBeanServerFactory}
 *
 * @version $Revision: 1.9 $ $Date: 2004/03/10 09:58:43 $
 */
public class ProxyContainer extends SimpleRPCContainer implements InvocationHandler {

    public ProxyContainer(Interceptor firstInterceptor) {
        super(firstInterceptor);
    }

    /**
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invocation invocation = new ProxyInvocation();
        ProxyInvocation.putMethod(invocation, method);
        ProxyInvocation.putArguments(invocation, args);
        ProxyInvocation.putProxy(invocation, proxy);
        InvocationResult result = this.invoke(invocation);
        if( result.isException() )
            throw result.getException();
        return result.getResult();
    }

    public Object createProxy(ClassLoader cl, Class[] interfaces) {
        return Proxy.newProxyInstance(cl, interfaces, this);
    }

    public static ProxyContainer getContainer(Object proxy) {
        if (Proxy.isProxyClass(proxy.getClass()))
            throw new IllegalArgumentException("Not a proxy.");
        return (ProxyContainer) Proxy.getInvocationHandler(proxy);
    }

}
