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
package org.apache.geronimo.j2ee.deployment;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.jar.JarFile;

import junit.framework.Assert;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.SchemaTypeLoader;

/**
 * @version $Revision: 1.5 $ $Date: 2004/08/09 04:19:35 $
 */
public class MockConnectorConfigBuilder extends Assert implements ModuleBuilder {
    public EARContext earContext;
    public Module connectorModule;
    public ClassLoader cl;

    public XmlObject getDeploymentPlan(URL module) {
        return null;
    }

    public boolean canHandlePlan(XmlObject plan) {
        return false;
    }

    public URI getParentId(XmlObject plan) {
        return null;
    }

    public URI getConfigId(XmlObject plan) {
        return null;
    }

    public Module createModule(String name, XmlObject plan) {
        return null;
    }

    public void installModule(File earFolder, EARContext earContext, Module connectorModule) {
        assertNotNull(earFolder);
        assertNotNull(earContext);
        this.earContext = earContext;
        assertEquals(this.connectorModule, connectorModule);
        if ( null != this.connectorModule.getAltSpecDD() ) {
            assertEquals(this.connectorModule.getAltSpecDD(), connectorModule.getAltSpecDD());
        }
        if ( null != this.connectorModule.getAltVendorDD() ) {
            assertEquals(this.connectorModule.getAltVendorDD(), connectorModule.getAltVendorDD());
        }
    }
    
    public void installModule(JarFile earFile, EARContext earContext, Module connectorModule) {
        assertNotNull(earFile);
        assertNotNull(earContext);
        this.earContext = earContext;
        assertEquals(this.connectorModule, connectorModule);
        if ( null != this.connectorModule.getAltSpecDD() ) {
            assertEquals(this.connectorModule.getAltSpecDD(), connectorModule.getAltSpecDD());
        }
        if ( null != this.connectorModule.getAltVendorDD() ) {
            assertEquals(this.connectorModule.getAltVendorDD(), connectorModule.getAltVendorDD());
        }
    }

    public void initContext(EARContext earContext, Module connectorModule, ClassLoader cl) {
        assertEquals(this.earContext, earContext);
        assertEquals(this.connectorModule, connectorModule);
        assertNotNull(cl);
        this.cl = cl;
    }

    public void addGBeans(EARContext earContext, Module connectorModule, ClassLoader cl) {
        assertEquals(this.earContext, earContext);
        assertEquals(this.connectorModule, connectorModule);
        assertEquals(this.cl, cl);
    }

    public SchemaTypeLoader getSchemaTypeLoader() {
        return null;
    }
}
