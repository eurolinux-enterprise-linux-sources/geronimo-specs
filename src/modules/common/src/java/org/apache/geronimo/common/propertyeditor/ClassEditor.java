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

package org.apache.geronimo.common.propertyeditor;

import org.apache.geronimo.kernel.ClassLoading;

/**
 * A property editor for {@link Class}.
 *
 * @version $Revision: 1.4 $ $Date: 2004/03/21 22:24:38 $
 */
public class ClassEditor
    extends TextPropertyEditorSupport
{
    /**
     * Returns a Class for the input object converted to a string.
     *
     * @return a Class object
     *
     * @throws PropertyEditorException   Failed to create Class instance.
     */
    public Object getValue()
    {
        try {
            String classname = getAsText();
            return ClassLoading.loadClass(classname);
        }
        catch (Exception e) {
            throw new PropertyEditorException(e);
        }
    }
}
