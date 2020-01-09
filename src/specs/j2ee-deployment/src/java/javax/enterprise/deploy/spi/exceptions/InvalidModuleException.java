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

package javax.enterprise.deploy.spi.exceptions;

/**
 * This exception is to report an invalid J2EE deployment module type.
 *
 * @version $Revision: 1.4 $ $Date: 2004/03/10 09:59:51 $
 */
public class InvalidModuleException extends Exception {
    /**
     * Creates a new InvalidModuleException.
     *
     * @param s a string indicating what was wrong with the module type.
     */
    public InvalidModuleException(String s) {
        super(s);
    }
}
