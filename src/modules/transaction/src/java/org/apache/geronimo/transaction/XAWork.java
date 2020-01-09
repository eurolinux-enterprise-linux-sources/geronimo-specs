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

package org.apache.geronimo.transaction;

import javax.transaction.xa.Xid;
import javax.transaction.xa.XAException;
import javax.transaction.SystemException;
import javax.transaction.InvalidTransactionException;

/**
 * primarily an interface between the WorkManager/ExecutionContext and the tm.
 *
 * @version $Revision: 1.3 $ $Date: 2004/07/11 21:25:21 $
 *
 * */
public interface XAWork {
    void begin(Xid xid, long txTimeout) throws XAException, InvalidTransactionException, SystemException;
    void end(Xid xid) throws XAException, SystemException;
}
