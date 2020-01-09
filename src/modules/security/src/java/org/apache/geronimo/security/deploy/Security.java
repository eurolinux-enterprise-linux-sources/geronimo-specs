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
package org.apache.geronimo.security.deploy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * @version $Revision: 1.4 $ $Date: 2004/07/27 01:57:06 $
 */
public class Security implements Serializable {

    private boolean doAsCurrentCaller;
    private boolean useContextHandler;
    private String defaultRole;
    private DefaultPrincipal defaultPrincipal;
    private Set roleMappings = new HashSet();

    public boolean isDoAsCurrentCaller() {
        return doAsCurrentCaller;
    }

    public void setDoAsCurrentCaller(boolean doAsCurrentCaller) {
        this.doAsCurrentCaller = doAsCurrentCaller;
    }

    public boolean isUseContextHandler() {
        return useContextHandler;
    }

    public void setUseContextHandler(boolean useContextHandler) {
        this.useContextHandler = useContextHandler;
    }

    public String getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    public DefaultPrincipal getDefaultPrincipal() {
        return defaultPrincipal;
    }

    public void setDefaultPrincipal(DefaultPrincipal defaultPrincipal) {
        this.defaultPrincipal = defaultPrincipal;
    }

    public Set getRoleMappings() {
        return roleMappings;
    }
}
