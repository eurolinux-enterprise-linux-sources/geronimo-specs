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

import java.beans.PropertyEditorSupport;

/**
 * A property editor support class for textual properties.
 *
 * @version $Revision: 1.3 $ $Date: 2004/03/10 09:58:26 $
 */
public class TextPropertyEditorSupport
    extends PropertyEditorSupport
{
    protected TextPropertyEditorSupport(final Object source)
    {
        super(source);
    }
    
    protected TextPropertyEditorSupport()
    {
        super();
    }
    
    /**
     * Sets the property value by parsing a given String.
     *
     * @param text  The string to be parsed.
     */
    public void setAsText(final String text)
    {
        setValue(text);
    }
}
