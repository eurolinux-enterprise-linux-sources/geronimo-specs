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

package org.apache.geronimo.messaging.interceptors;

import org.apache.geronimo.messaging.Msg;

/**
 * Inbound Msg interceptor. It allows to pop Msgs from a Msg source, which can
 * be various thing: a queue, an InputStream, another inbound Msg interceptor
 * et cetera.
 * <BR>
 * It is also in charge of adding various specificities popped Msgs.
 *
 * @version $Revision: 1.1 $ $Date: 2004/05/11 12:06:40 $
 */
public interface MsgInInterceptor {
  
    /**
     * Pops a Msg from the underlying Msg source.
     * 
     * @return Msg.
     */
    public Msg pop();
    
}
