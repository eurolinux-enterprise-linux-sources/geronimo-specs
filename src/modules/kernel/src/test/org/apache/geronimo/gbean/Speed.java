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
package org.apache.geronimo.gbean;

/**
 * @version $Revision: 1.3 $ $Date: 2004/06/03 15:27:28 $
 */
import java.lang.reflect.Method;

import javax.management.ObjectName;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.geronimo.gbean.jmx.CGLibProxyFactory;
import org.apache.geronimo.gbean.jmx.ProxyFactory;
import org.apache.geronimo.gbean.jmx.ProxyMethodInterceptor;
import org.apache.geronimo.gbean.jmx.VMProxyFactory;
import org.apache.geronimo.gbean.jmx.GBeanMBean;
import org.apache.geronimo.gbean.jmx.CGLibMethodInterceptor;
import org.apache.geronimo.gbean.jmx.RawInvoker;
import org.apache.geronimo.kernel.MockGBean;
import org.apache.geronimo.kernel.Kernel;

/**
 * @version $Revision: 1.3 $ $Date: 2004/06/03 15:27:28 $
 */
public class Speed {
    private static final Object[] NO_ARGS = new Object[0];

    public static void main(String[] ignored) throws Exception {
        System.out.println("Do Nothing Timings");
        System.out.println("------------------");
        doNothingTimings();
        System.out.println();
        System.out.println();
        System.out.println();
        doNothingTimings();
//        System.out.println("Echo Timings");
//        System.out.println("-------------");
//        echoTimings();
    }

    private static void doNothingTimings() throws Exception {
        Method myMethod = MockGBean.class.getMethod("doNothing", null);

        FastClass myFastClass = FastClass.create(MockGBean.class);
        int myMethodIndex = myFastClass.getIndex("doNothing", new Class[0]);

        MockGBean instance = new MockGBean("foo", 12);

        // normal invoke
        int iterations = 100000000;
        String msg = "hhhh";
        for (int j=0 ; j < 10; j++) {
            for (int i = 0; i < iterations; i++) {
                msg= instance.echo(msg);
            }
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            instance.doNothing();
        }
        long end = System.currentTimeMillis();
        printResults("Normal", end, start, iterations);

        // reflection
        iterations = 1000000;
        for (int i = 0; i < iterations; i++) {
            myMethod.invoke(instance, null);
        }
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            myMethod.invoke(instance, null);
        }
        end = System.currentTimeMillis();
        printResults("Reflection", end, start, iterations);

        // fast class
        iterations = 5000000;
        for (int i = 0; i < iterations; i++) {
            myFastClass.invoke(myMethodIndex, instance, null);
        }
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            myFastClass.invoke(myMethodIndex, instance, null);
        }
        end = System.currentTimeMillis();
        printResults("FastClass", end, start, iterations);

        // start a kernel
        Kernel kernel = new Kernel();
        kernel.boot();
        GBeanMBean mockGBean = new GBeanMBean(MockGBean.getGBeanInfo(), Speed.class.getClassLoader());
        mockGBean.setAttribute("Name", "bar");
        mockGBean.setAttribute("FinalInt", new Integer(57));
        ObjectName objectName = new ObjectName("speed:type=MockGBean");
        kernel.loadGBean(objectName, mockGBean);
        kernel.startGBean(objectName);

        // reflect proxy
        ProxyFactory vmProxyFactory = new VMProxyFactory(MyInterface.class);
        ProxyMethodInterceptor vmMethodInterceptor = vmProxyFactory.getMethodInterceptor();
        MyInterface vmProxy = (MyInterface) vmProxyFactory.create(vmMethodInterceptor);
        vmMethodInterceptor.connect(kernel.getMBeanServer(), objectName);
        iterations = 50000;
        for (int i = 0; i < iterations; i++) {
            vmProxy.doNothing();
        }
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            vmProxy.doNothing();
        }
        end = System.currentTimeMillis();
        printResults("ReflectionProxy", end, start, iterations);

        // cglib proxy (front half)
/*
        ProxyFactory frontCGLibProxyFactory = new CGLibProxyFactory(MyInterface.class);
        ProxyMethodInterceptor frontCGLibMethodInterceptor = new CGLibMethodInterceptor(MyInterface.class);
        Class enhancedType = frontCGLibProxyFactory.create(frontCGLibMethodInterceptor).getClass();
        frontCGLibMethodInterceptor = new CGLibMethodInterceptor(enhancedType) {
            public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return null;
            }
        };
        MyInterface frontCGLibProxy = (MyInterface) frontCGLibProxyFactory.create(frontCGLibMethodInterceptor);
        frontCGLibMethodInterceptor.connect(kernel.getMBeanServer(), objectName);
        iterations = 100000000;
        for (int i = 0; i < iterations; i++) {
            frontCGLibProxy.doNothing();
        }
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            frontCGLibProxy.doNothing();
        }
        end = System.currentTimeMillis();
        printResults("Front CGLibProxy", end, start, iterations);
*/

        // Raw Invoker
        RawInvoker rawInvoker = (RawInvoker) kernel.getAttribute(objectName, "$$RAW_INVOKER$$");
        int rawIndex = ((Integer) rawInvoker.getOperationIndex().get(new GOperationSignature("doNothing", new String[0]))).intValue();
        iterations = 2000000;
        for (int i = 0; i < iterations; i++) {
            rawInvoker.invoke(rawIndex, NO_ARGS);
        }
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            rawInvoker.invoke(rawIndex, NO_ARGS);
        }
        end = System.currentTimeMillis();
        printResults("Raw Invoker", end, start, iterations);

        // cglib proxy
        ProxyFactory cgLibProxyFactory = new CGLibProxyFactory(MyInterface.class);
        ProxyMethodInterceptor cgLibMethodInterceptor = cgLibProxyFactory.getMethodInterceptor();
        MyInterface cgLibProxy = (MyInterface) cgLibProxyFactory.create(cgLibMethodInterceptor);
        cgLibMethodInterceptor.connect(kernel.getMBeanServer(), objectName);
        iterations = 1000000;
        for (int i = 0; i < iterations; i++) {
            cgLibProxy.doNothing();
        }
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            cgLibProxy.doNothing();
        }
        end = System.currentTimeMillis();
        printResults("CGLibProxy", end, start, iterations);
    }


    private static void echoTimings() throws Exception {
        Method myMethod = MockGBean.class.getMethod("echo", new Class[]{String.class});

        FastClass myFastClass = FastClass.create(MockGBean.class);
        int myMethodIndex = myFastClass.getIndex("echo", new Class[]{String.class});
        String msg = "Some message";
        Object[] args = new Object[]{msg};
        String result;

        MockGBean instance = new MockGBean("foo", 12);


        // normal invoke
        int iterations = 100000000;
        for (int i = 0; i < iterations; i++) {
            result = instance.echo(msg);
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            result = instance.echo(msg);
        }
        long end = System.currentTimeMillis();
        printResults("Normal", end, start, iterations);

        // reflection
        iterations = 10000000;
        for (int i = 0; i < iterations; i++) {
            result = (String) myMethod.invoke(instance, args);
        }
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            result = (String) myMethod.invoke(instance, args);
        }
        end = System.currentTimeMillis();
        printResults("Reflection", end, start, iterations);

        // fast class
        iterations = 10000000;
        for (int i = 0; i < iterations; i++) {
            result = (String) myFastClass.invoke(myMethodIndex, instance, args);
        }
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            result = (String) myFastClass.invoke(myMethodIndex, instance, args);
        }
        end = System.currentTimeMillis();
        printResults("FastClass", end, start, iterations);

        // start a kernel
        Kernel kernel = new Kernel();
        kernel.boot();
        GBeanMBean mockGBean = new GBeanMBean(MockGBean.getGBeanInfo(), Speed.class.getClassLoader());
        mockGBean.setAttribute("Name", "bar");
        mockGBean.setAttribute("FinalInt", new Integer(57));
        ObjectName objectName = new ObjectName("speed:type=MockGBean");
        kernel.loadGBean(objectName, mockGBean);
        kernel.startGBean(objectName);

        // reflect proxy
        ProxyFactory vmProxyFactory = new VMProxyFactory(MyInterface.class);
        ProxyMethodInterceptor vmMethodInterceptor = vmProxyFactory.getMethodInterceptor();
        MyInterface vmProxy = (MyInterface) vmProxyFactory.create(vmMethodInterceptor);
        vmMethodInterceptor.connect(kernel.getMBeanServer(), objectName);
        iterations = 50000;
        for (int i = 0; i < iterations; i++) {
            result = vmProxy.echo(msg);
        }
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            result = vmProxy.echo(msg);
        }
        end = System.currentTimeMillis();
        printResults("ReflectionProxy", end, start, iterations);

        // cglib proxy
        ProxyFactory cgLibProxyFactory = new CGLibProxyFactory(MyInterface.class);
        ProxyMethodInterceptor cgLibMethodInterceptor = cgLibProxyFactory.getMethodInterceptor();
        MyInterface cgLibProxy = (MyInterface) cgLibProxyFactory.create(cgLibMethodInterceptor);
        cgLibMethodInterceptor.connect(kernel.getMBeanServer(), objectName);
        iterations = 1000000;
        for (int i = 0; i < iterations; i++) {
            result = cgLibProxy.echo(msg);
        }
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            result = cgLibProxy.echo(msg);
        }
        end = System.currentTimeMillis();
        printResults("CGLibProxy", end, start, iterations);
    }

    private static void printResults(String invocationType, long end, long start, int iterations) {
        if(end - start < 400) {
            System.out.println(invocationType + ": elapse time to short to calculate cost (total " + (end - start) + "ms)");
        } else {
            System.out.println(invocationType + ": " + ((end - start) * 1000000.0 / iterations) + "ns  (total " + (end - start) + "ms)");
        }
    }

    public static interface MyInterface {
        void doNothing();
        String echo(String msg);
    }
}
