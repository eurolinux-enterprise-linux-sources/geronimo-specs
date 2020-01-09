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

package org.apache.geronimo.deployment.plugin;

import javax.enterprise.deploy.spi.Target;
import javax.management.ObjectName;

/**
 *
 *
 * @version $Revision: 1.5 $ $Date: 2004/06/02 19:50:40 $
 */
public class TargetImpl implements Target {
    private final ObjectName name;
    private final String description;

    public TargetImpl(ObjectName name, String description) {
        this.name = name;
        this.description = description;
    }

    public ObjectName getObjectName() {
        return name;
    }

    public String getName() {
        return name.toString();
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return name.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TargetImpl)) return false;

        final TargetImpl target = (TargetImpl) o;

        if (!name.equals(target.name)) return false;

        return true;
    }

    public int hashCode() {
        return name.hashCode();
    }
}
