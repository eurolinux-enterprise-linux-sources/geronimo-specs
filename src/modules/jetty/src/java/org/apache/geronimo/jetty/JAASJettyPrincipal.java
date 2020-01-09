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
package org.apache.geronimo.jetty;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Stack;


/**
 * @version $Revision: 1.2 $ $Date: 2004/06/27 20:37:38 $
 */
public class JAASJettyPrincipal implements Principal {
    private String name;
    private Subject subject;
    private Stack stack = new Stack();

    public JAASJettyPrincipal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Subject getSubject() {
        return subject;
    }

    void setSubject(Subject subject) {
        this.subject = subject;
    }

    void push(Subject roleDesignate) {
        stack.push(roleDesignate);
    }

    Subject pop() {
        return (Subject) stack.pop();
    }
}
