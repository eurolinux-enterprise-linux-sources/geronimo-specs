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

package javax.transaction;

/**
 *
 *
 *
 * @version $Revision: 1.3 $ $Date: 2004/03/10 10:00:12 $
 */
public interface Status {
    int STATUS_ACTIVE = 0;
    int STATUS_MARKED_ROLLBACK = 1;
    int STATUS_PREPARED = 2;
    int STATUS_COMMITTED = 3;
    int STATUS_ROLLEDBACK = 4;
    int STATUS_UNKNOWN = 5;
    int STATUS_NO_TRANSACTION = 6;
    int STATUS_PREPARING = 7;
    int STATUS_COMMITTING = 8;
    int STATUS_ROLLING_BACK = 9;
}
