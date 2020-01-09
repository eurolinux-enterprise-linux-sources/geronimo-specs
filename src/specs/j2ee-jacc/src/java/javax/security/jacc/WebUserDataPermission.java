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

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//

package javax.security.jacc;

import java.io.IOException;

import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import java.security.Permission;

import java.security.PermissionCollection;

import javax.servlet.http.HttpServletRequest;


/**
 * Class for Servlet Web user data permissions. A WebUserDataPermission is a
 * named permission and has actions.<p>
 *
 * The name of a WebUserDataPermission (also referred to as the target name)
 * identifies a Web resource by its context path relative URL pattern.
 *
 * @version $Revision: 1.5 $ $Date: 2004/07/25 00:57:35 $
 * @see java.security.Permission
 */
public final class WebUserDataPermission extends Permission {

    private transient int cachedHashCode = 0;
    private transient URLPatternSpec urlPatternSpec;
    private transient HTTPMethodSpec httpMethodSpec;

    /**
     * Creates a new WebUserDataPermission from the HttpServletRequest object.
     * 
     * @param request the HttpServletRequest object corresponding to the
     * Servlet operation to which the permission pertains. The permission
     * name is the substring of the requestURI (HttpServletRequest.getRequestURI())
     * that begins after the contextPath (HttpServletRequest.getContextPath()).
     * When the substring operation yields the string �/�, the permission is
     * constructed with the empty string as its name. The HTTP method component
     * of the permission�s actions is as obtained from HttpServletRequest.getMethod().
     * The TransportType component of the permission�s actions is determined
     * by calling HttpServletRequest.isSecure().
     */
    public WebUserDataPermission(HttpServletRequest request) {
        super(request.getServletPath());

        urlPatternSpec = new URLPatternSpec(request.getServletPath());
        httpMethodSpec = new HTTPMethodSpec(request.getMethod());
    }

    public WebUserDataPermission(String name, String actions) {
        super(name);

        urlPatternSpec = new URLPatternSpec(name);
        httpMethodSpec = new HTTPMethodSpec(actions, true);
    }

    public WebUserDataPermission(String urlPattern, String[] HTTPMethods, String transportType) {
        super(urlPattern);

        urlPatternSpec = new URLPatternSpec(urlPattern);
        httpMethodSpec = new HTTPMethodSpec(HTTPMethods, transportType);
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof WebUserDataPermission)) return false;

        WebUserDataPermission other = (WebUserDataPermission)o;
        return urlPatternSpec.equals(other.urlPatternSpec) && httpMethodSpec.equals(other.httpMethodSpec);
    }

    public String getActions() {
        return httpMethodSpec.getActions();
    }

    public int hashCode() {
        if (cachedHashCode == 0) {
            cachedHashCode = urlPatternSpec.hashCode() ^ httpMethodSpec.hashCode();
        }
        return cachedHashCode;
    }

    public boolean implies(Permission permission) {
        if (permission == null || !(permission instanceof WebUserDataPermission)) return false;

        WebUserDataPermission other = (WebUserDataPermission)permission;
        return urlPatternSpec.implies(other.urlPatternSpec) && httpMethodSpec.implies(other.httpMethodSpec);
    }

    public PermissionCollection newPermissionCollection() {
    	return new WebUserDataPermissionCollection();
    }

    private synchronized void readObject(ObjectInputStream in) throws IOException {
        urlPatternSpec = new URLPatternSpec(in.readUTF());
        httpMethodSpec = new HTTPMethodSpec(in.readUTF());
    }

    private synchronized void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(urlPatternSpec.getPatternSpec());
        out.writeUTF(httpMethodSpec.getActions());
    }
}



