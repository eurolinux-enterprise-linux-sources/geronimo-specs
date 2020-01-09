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

package javax.servlet.http;

import java.util.Enumeration;

/**
 * @deprecated As of Java(tm) Servlet API 2.1 for security reasons, with no
 * replacement. This interface will be removed in a future version of this API.
 *
 * @see HttpSession
 * @see HttpSessionBindingEvent
 * @see HttpSessionBindingListener
 *
 * @version $Revision: 1.4 $ $Date: 2004/03/10 10:00:20 $
 */
public interface HttpSessionContext {
    /**
     * @deprecated As of Java Servlet API 2.1 with no replacement. This
     * method must return null and will be removed in a future version of this
     * API.
     */
    public HttpSession getSession(String sessionId);

    /**
     * @deprecated As of Java Servlet API 2.1 with no replacement. This
     * method must return an empty <code>Enumeration</code> and will be removed
     * in a future version of this API.
     */
    public Enumeration getIds();
}





