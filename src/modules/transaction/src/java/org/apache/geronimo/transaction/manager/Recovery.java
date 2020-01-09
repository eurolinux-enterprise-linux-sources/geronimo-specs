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

package org.apache.geronimo.transaction.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.HashSet;

import javax.transaction.SystemException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *
 * @version $Revision: 1.5 $ $Date: 2004/07/22 03:39:01 $
 *
 * */
public interface Recovery {

    void recoverLog() throws XAException;

    void recoverResourceManager(NamedXAResource xaResource) throws XAException;

    boolean hasRecoveryErrors();

    List getRecoveryErrors();

    boolean localRecoveryComplete();

    int localUnrecoveredCount();

    //hard to implement.. needs ExternalTransaction to have a reference to externalXids.
//    boolean remoteRecoveryComplete();

    Map getExternalXids();

    public static class XidBranchesPair {
        private final Xid xid;

        //set of TransactionBranchInfo
        private final Set branches = new HashSet();

        private final long mark;

        public XidBranchesPair(Xid xid, long mark) {
            this.xid = xid;
            this.mark = mark;
        }

        public Xid getXid() {
            return xid;
        }

        public Set getBranches() {
            return branches;
        }

        public long getMark() {
            return mark;
        }

        public void addBranch(TransactionBranchInfo branchInfo) {
            branches.add(branchInfo);
        }
    }

}
