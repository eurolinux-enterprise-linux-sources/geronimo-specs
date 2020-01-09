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

package javax.ejb.spi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;

/**
 *
 *
 *
 * @version $Revision: 1.3 $ $Date: 2004/03/10 09:59:40 $
 */
public interface HandleDelegate {
    EJBHome readEJBHome(ObjectInputStream istream) throws ClassNotFoundException, IOException;

    EJBObject readEJBObject(ObjectInputStream istream) throws ClassNotFoundException, IOException;

    void writeEJBHome(EJBHome ejbHome, ObjectOutputStream ostream) throws IOException;

    void writeEJBObject(EJBObject ejbObject, ObjectOutputStream ostream) throws IOException;
}
