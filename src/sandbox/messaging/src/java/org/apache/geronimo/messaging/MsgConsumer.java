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

package org.apache.geronimo.messaging;

import org.apache.geronimo.messaging.interceptors.MsgOutInterceptor;

/**
 * Components consuming Msgs implement this interface. 
 *
 * @version $Revision: 1.1 $ $Date: 2004/05/11 12:06:41 $
 */
public interface MsgConsumer
{

    /**
     * Gets a mean to push Msgs to this instance.
     *  
     * @return Mean to push Msgs to this consumer of Msgs.
     */
    public MsgOutInterceptor getMsgConsumerOut();

}
