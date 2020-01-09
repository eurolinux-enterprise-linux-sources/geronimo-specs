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

package org.apache.geronimo.remoting;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.geronimo.proxy.ProxyContainer;
import org.apache.geronimo.proxy.ReflexiveInterceptor;
import org.apache.geronimo.remoting.transport.BytesMarshalledObject;

/**
 * Unit test for the Marshaling/DeMarshaling Interceptors
 *
 * This test uses 2 classloaders to mock 2 seperate
 * application classloaders.
 *
 * @version $Revision: 1.5 $ $Date: 2004/09/06 11:14:13 $
 */

public class MarshalingInterceptorsTest extends TestCase {
    private static final File basedir = new File(System.getProperty("basedir", System.getProperty("user.dir")));

    private static final String PERSON_CLASS = "org.apache.geronimo.remoting.Person";
    private static final String TRANSIENT_CLASS = "org.apache.geronimo.remoting.TransientValue";
    ArrayList severContainers = new ArrayList();
    URLClassLoader cl1, cl2;

    /**
     * creates the two classloaders that will be used during the tests.
     */
    public void setUp() throws MalformedURLException, IOException {
        URL url = new File(basedir, "target/mock-app").toURL();
        System.out.println("Setting up the CP to: " + url);

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        cl1 = new URLClassLoader(new URL[] { url }, cl);
        cl2 = new URLClassLoader(new URL[] { url }, cl);
    }    

    /**
     * Verify that the classpath/classloader structure is
     * as expected.  If the application classes are in
     * the current classloader then that will throw all the 
     * tests off.
     * 
     * @throws Exception
     */
    public void testCheckClassLoaders() throws Exception {
        Class class1 = cl1.loadClass(PERSON_CLASS);
        Class class2 = cl2.loadClass(PERSON_CLASS);
        assertFalse("Classpath for this test was incorrect.  The '"+PERSON_CLASS+"' cannot be in the test's classpath.", class1.equals(class2));
    }

    /**
     * Verify that the helper methods used by this test work
     * as expected.  Everything should be ok when working in the
     * same classloader.
     * 
     * @throws Throwable
     */
    public void testSetSpouseSameCL() throws Throwable {
        Class class1 = cl1.loadClass(PERSON_CLASS);
        Class class2 = cl1.loadClass(PERSON_CLASS);

        Object object1 = class1.newInstance();
        Object object2 = class2.newInstance();

        call(object1, "setSpouse", new Object[] { object2 });
    }

    /**
     * Verify that the helper methods used by this test work
     * as expected.  We should see problems when you start mixing
     * Objects from different classloaders.
     * 
     * @throws Throwable
     */
    public void testSetSpouseOtherCL() throws Throwable {
        Class class1 = cl1.loadClass(PERSON_CLASS);
        Class class2 = cl2.loadClass(PERSON_CLASS);

        Object object1 = class1.newInstance();
        Object object2 = class2.newInstance();

        try {
            call(object1, "setSpouse", new Object[] { object2 });
            fail("Call should fail due to argument type mismatch.");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Verify that a proxy using working in a single app context works ok.
     * 
     * @throws Throwable
     */
    public void testSetSpouseWithProxy() throws Throwable {
        Class class1 = cl1.loadClass(PERSON_CLASS);
        Object object1 = class1.newInstance();

        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
        
        // Simulate App1 creating a proxy.
        Thread.currentThread().setContextClassLoader(cl1);
        Object proxy1 = createProxy(object1);
        call(proxy1, "setSpouse", new Object[] { object1 });

        Thread.currentThread().setContextClassLoader(oldCL);
    }

    /**
     * App2 creates a proxy and serializes.  App1 context deserializes
     * the proxy and trys to use it.  Method calls should be getting
     * marshalled/demarshaled by the proxy to avoid getting an 
     * IllegalArgumentException.
     * 
     * @throws Throwable
     */
    public void testSetSpouseOtherCLWithSerializedProxy() throws Throwable {
        Class class1 = cl1.loadClass(PERSON_CLASS);
        Class class2 = cl2.loadClass(PERSON_CLASS);

        Object object1 = class1.newInstance();
        Object object2 = class2.newInstance();

        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
        
        // Simulate App2 creating a proxy.
        Thread.currentThread().setContextClassLoader(cl2);
        Object proxy2 = createProxy(object2);
        MarshalledObject mo = new BytesMarshalledObject(proxy2);

        // Simulate App1 using the serialized proxy.    
        Thread.currentThread().setContextClassLoader(cl1);
        Object proxy1 = mo.get();
        call(proxy1, "setSpouse", new Object[] { object1 });
        
        Thread.currentThread().setContextClassLoader(oldCL);
    }

    /**
     * App1 creates a proxy.  It then creates a Transient calls that holds
     * a "value" property that is transient when serialized.  This allows to 
     * to see if the Transient object was serialized by getting the value back
     * and seeing if the "value" is null.  All this work is done in one classloader
     * so no serialization should occur.
     * 
     * @throws Throwable
     */
    public void testSetTransientWithOptimizedProxy() throws Throwable {
        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
        
        Thread.currentThread().setContextClassLoader(cl1);
        Class class1 = cl1.loadClass(PERSON_CLASS);
        Object object1 = class1.newInstance();

        Class class2 = cl1.loadClass(TRANSIENT_CLASS);
        Object object2 = class2.newInstance();
        call(object2, "setValue", new Object[] { "foo" });

        Object proxy1 = createProxy(object1);
        call(proxy1, "setValue", new Object[] { object2 });
        Object rc = call(proxy1, "getValue", new Object[] {
        });
        rc = call(rc, "getValue", new Object[] {
        });

        assertSame(rc, "foo");
        
        Thread.currentThread().setContextClassLoader(oldCL);
    }

    /**
     * Same as testSetTransientWithOptimizedProxy() but, the proxy is serialized before it is used.
     * It should still result in the method call not being serialized.
     * 
     * @throws Throwable
     */
    public void testSetTransientWithSerializedOptimizedProxy() throws Throwable {
        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
        
        Thread.currentThread().setContextClassLoader(cl1);
        Class class1 = cl1.loadClass(PERSON_CLASS);
        Object object1 = class1.newInstance();

        Class class2 = cl1.loadClass(TRANSIENT_CLASS);
        Object object2 = class2.newInstance();
        call(object2, "setValue", new Object[] { "foo" });

        Object proxy1 = createProxy(object1);
        proxy1 = new BytesMarshalledObject(proxy1).get();
        call(proxy1, "setValue", new Object[] { object2 });
        Object rc = call(proxy1, "getValue", new Object[] {
        });
        rc = call(rc, "getValue", new Object[] {
        });

        assertSame(rc, "foo");
        
        Thread.currentThread().setContextClassLoader(oldCL);
    }

    /**
     * Same as testSetTransientWithOptimizedProxy() but, the proxy is serialized before it is
     * by App2.  Since a different classloader is using the proxy, the metod call should
     * be serialized and we should see that the set "value" is null.
     * 
     * @throws Throwable
     */
    public void testSetTransientWithSerializedNonOptimizedProxy() throws Throwable {
        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();

        Thread.currentThread().setContextClassLoader(cl1);
        Class class1 = cl1.loadClass(PERSON_CLASS);
        Object object1 = class1.newInstance();

        Object proxy1 = createProxy(object1);
        Thread.currentThread().setContextClassLoader(cl2);
        proxy1 = new BytesMarshalledObject(proxy1).get();

        Class class2 = cl2.loadClass(TRANSIENT_CLASS);
        Object object2 = class2.newInstance();
        call(object2, "setValue", new Object[] { "foo" });

        call(proxy1, "setValue", new Object[] { object2 });
        Object rc = call(proxy1, "getValue", new Object[] {
        });
        rc = call(rc, "getValue", new Object[] {
        });

        assertSame(rc, null);

        Thread.currentThread().setContextClassLoader(oldCL);
    }

    /**
     * Does a reflexive call on object1 my calling method with the provided args.
     * 
     * @param object1
     */
    private Object call(Object object1, String method, Object[] args) throws Throwable {
        try {
            Class argTypes[] = getArgTypes(object1.getClass().getClassLoader(), args);
            Method m = findMethod(object1.getClass(), method);
            return m.invoke(object1, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    /**
     * Gets the Class[] for a given Object[] using the provided loader.
     * 
     * @param args
     * @return
     */
    private Class[] getArgTypes(ClassLoader loader, Object[] args) throws Exception {
        Class rc[] = new Class[args.length];
        for (int i = 0; i < rc.length; i++) {
            rc[i] = loader.loadClass(args[i].getClass().getName());
        }
        return rc;
    }

    /**
     * Finds the first method in class c whose name is name.
     * @param c
     * @param name
     * @return
     */
    private Method findMethod(Class c, String name) {
        Method[] methods = c.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(name))
                return methods[i];
        }
        return null;
    }

    /**
      * @return
      */
    private Object createProxy(Object target) throws Exception {
        
        // Setup the server side contianer..
        ReflexiveInterceptor ri = new ReflexiveInterceptor(target);
        DeMarshalingInterceptor demarshaller = new DeMarshalingInterceptor(ri, target.getClass().getClassLoader());
        ProxyContainer serverContainer = new ProxyContainer(demarshaller);

        // Configure the server side interceptors.
        Long dmiid = InterceptorRegistry.instance.register(demarshaller);
        
        // Setup the client side container..        
        IntraVMRoutingInterceptor localRouter = new IntraVMRoutingInterceptor(ri, dmiid, false);
        ProxyContainer clientContainer = new ProxyContainer(localRouter);
        return clientContainer.createProxy(target.getClass().getClassLoader(), target.getClass().getInterfaces());
    }

}
