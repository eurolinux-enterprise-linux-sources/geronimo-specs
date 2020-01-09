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

/**
 * Exception to be raised when a communication problem occurs.
 *
 * @version $Revision: 1.2 $ $Date: 2004/06/03 14:34:04 $
 */
public class CommunicationException extends RuntimeException
{

    public CommunicationException(String aMessage) {
        super(aMessage);
    }
    
    public CommunicationException(Throwable aNested) {
        super(aNested);
    }
    
    public CommunicationException(String aMessage, Throwable aNested) {
        super(aMessage, aNested);
    }
    
}
