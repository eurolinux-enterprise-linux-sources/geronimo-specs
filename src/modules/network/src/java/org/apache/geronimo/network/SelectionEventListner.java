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

package org.apache.geronimo.network;

import java.nio.channels.SelectionKey;


/**
 * This is the interface that the attachments of SelectorKeys
 * that are registed with the Selector of the SelectorManager manager
 * must implement.
 *
 * @version $Revision: 1.3 $ $Date: 2004/05/04 03:05:36 $
 */
public interface SelectionEventListner {

    /**
     * When the SelectorKey is triggered, the service method will
     * be called on the attachment.
     */
    public void selectionEvent(SelectorManager.Event event);
}
