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

package org.apache.geronimo.security.jaas;

import javax.management.ObjectName;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.geronimo.gbean.jmx.GBeanMBean;
import org.apache.geronimo.kernel.Kernel;
import org.apache.geronimo.security.AbstractTest;
import org.apache.geronimo.security.ContextManager;
import org.apache.geronimo.security.IdentificationPrincipal;
import org.apache.geronimo.security.RealmPrincipal;
import org.apache.geronimo.system.serverinfo.ServerInfo;


/**
 * @version $Revision: 1.10 $ $Date: 2004/07/12 06:07:51 $
 */
public class ConfigurationEntryTest extends TestCase {

    protected Kernel kernel;
    protected ObjectName serverInfo;
    protected ObjectName loginConfiguration;
    protected ObjectName loginService;
    protected ObjectName testCE;
    protected ObjectName testRealm;
    protected ObjectName subsystemRouter;
    protected ObjectName asyncTransport;
    protected ObjectName jmxRouter;
    protected ObjectName serverStub;

    public void test() throws Exception {
        LoginContext context = new LoginContext("properties", new AbstractTest.UsernamePasswordCallback("alan", "starcraft"));

        context.login();
        Subject subject = context.getSubject();

        assertTrue("expected non-null subject", subject != null);
        assertTrue("subject should have one remote principal", subject.getPrincipals(IdentificationPrincipal.class).size() == 1);
        IdentificationPrincipal remote = (IdentificationPrincipal) subject.getPrincipals(IdentificationPrincipal.class).iterator().next();
        assertTrue("subject should be associated with remote id", ContextManager.getRegisteredSubject(remote.getId()) != null);
        assertTrue("subject should have five principals", subject.getPrincipals().size() == 5);
        assertTrue("subject should have two realm principal", subject.getPrincipals(RealmPrincipal.class).size() == 2);
        RealmPrincipal principal = (RealmPrincipal) subject.getPrincipals(RealmPrincipal.class).iterator().next();
        assertTrue("id of principal should be non-zero", principal.getId() != 0);

        context.logout();

        assertTrue("id of subject should be null", ContextManager.getSubjectId(subject) == null);
    }

    protected void setUp() throws Exception {
        kernel = new Kernel("test.kernel", "simple.geronimo.test");
        kernel.boot();

        GBeanMBean gbean;

        // Create all the parts

        gbean = new GBeanMBean(ServerInfo.GBEAN_INFO);
        serverInfo = new ObjectName("geronimo.system:role=ServerInfo");
        gbean.setAttribute("baseDirectory", ".");
        kernel.loadGBean(serverInfo, gbean);
        kernel.startGBean(serverInfo);

        gbean = new GBeanMBean("org.apache.geronimo.security.jaas.GeronimoLoginConfiguration");
        loginConfiguration = new ObjectName("geronimo.security:type=LoginConfiguration");
        kernel.loadGBean(loginConfiguration, gbean);

        gbean = new GBeanMBean("org.apache.geronimo.security.jaas.LoginService");
        loginService = new ObjectName("geronimo.security:type=LoginService");
        gbean.setReferencePatterns("Realms", Collections.singleton(new ObjectName("geronimo.security:type=SecurityRealm,*")));
        gbean.setAttribute("reclaimPeriod", new Long(100));
        gbean.setAttribute("algorithm", "HmacSHA1");
        gbean.setAttribute("password", "secret");
        kernel.loadGBean(loginService, gbean);

        gbean = new GBeanMBean("org.apache.geronimo.security.jaas.ConfigurationEntryRealmLocal");
        testCE = new ObjectName("geronimo.security:type=ConfigurationEntry,jaasId=properties");
        gbean.setAttribute("applicationConfigName", "properties");
        gbean.setAttribute("realmName", "properties-realm");
        gbean.setAttribute("controlFlag", LoginModuleControlFlag.REQUIRED);
        gbean.setAttribute("options", new Properties());
        kernel.loadGBean(testCE, gbean);

        gbean = new GBeanMBean("org.apache.geronimo.security.realm.providers.PropertiesFileSecurityRealm");
        testRealm = new ObjectName("geronimo.security:type=SecurityRealm,realm=properties-realm");
        gbean.setAttribute("realmName", "properties-realm");
        gbean.setAttribute("maxLoginModuleAge", new Long(1 * 1000));
        gbean.setAttribute("usersURI", (new File(new File("."), "src/test-data/data/users.properties")).toURI());
        gbean.setAttribute("groupsURI", (new File(new File("."), "src/test-data/data/groups.properties")).toURI());
        gbean.setReferencePatterns("ServerInfo", Collections.singleton(serverInfo));
        kernel.loadGBean(testRealm, gbean);

        gbean = new GBeanMBean("org.apache.geronimo.remoting.router.SubsystemRouter");
        subsystemRouter = new ObjectName("geronimo.remoting:router=SubsystemRouter");
        kernel.loadGBean(subsystemRouter, gbean);

        gbean = new GBeanMBean("org.apache.geronimo.remoting.transport.TransportLoader");
        gbean.setAttribute("bindURI", new URI("async://0.0.0.0:4242"));
        gbean.setReferencePatterns("Router", Collections.singleton(subsystemRouter));
        asyncTransport = new ObjectName("geronimo.remoting:transport=async");
        kernel.loadGBean(asyncTransport, gbean);

        gbean = new GBeanMBean("org.apache.geronimo.remoting.router.JMXRouter");
        gbean.setReferencePatterns("SubsystemRouter", Collections.singleton(subsystemRouter));
        jmxRouter = new ObjectName("geronimo.remoting:router=JMXRouter");
        kernel.loadGBean(jmxRouter, gbean);

        gbean = new GBeanMBean("org.apache.geronimo.security.remoting.jmx.LoginServiceStub");
        gbean.setReferencePatterns("Router", Collections.singleton(jmxRouter));
        serverStub = new ObjectName("geronimo.remoting:target=LoginServiceStub");
        kernel.loadGBean(serverStub, gbean);

        kernel.startGBean(loginConfiguration);
        kernel.startGBean(loginService);
        kernel.startGBean(testCE);
        kernel.startGBean(testRealm);
        kernel.startGBean(subsystemRouter);
        kernel.startGBean(asyncTransport);
        kernel.startGBean(jmxRouter);
        kernel.startGBean(serverStub);
    }

    protected void tearDown() throws Exception {
        kernel.stopGBean(serverStub);
        kernel.stopGBean(jmxRouter);
        kernel.stopGBean(asyncTransport);
        kernel.stopGBean(subsystemRouter);
        kernel.stopGBean(testRealm);
        kernel.stopGBean(testCE);
        kernel.stopGBean(loginService);
        kernel.stopGBean(loginConfiguration);
        kernel.stopGBean(serverInfo);

        kernel.unloadGBean(loginService);
        kernel.unloadGBean(testCE);
        kernel.unloadGBean(testRealm);
        kernel.unloadGBean(subsystemRouter);
        kernel.unloadGBean(asyncTransport);
        kernel.unloadGBean(jmxRouter);
        kernel.unloadGBean(serverStub);
        kernel.unloadGBean(loginConfiguration);
        kernel.unloadGBean(serverInfo);

        kernel.shutdown();
    }
}
