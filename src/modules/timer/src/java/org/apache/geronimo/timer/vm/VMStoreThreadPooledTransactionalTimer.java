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

package org.apache.geronimo.timer.vm;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import org.apache.geronimo.gbean.GBeanInfo;
import org.apache.geronimo.gbean.GBeanInfoFactory;
import org.apache.geronimo.timer.PersistentTimer;
import org.apache.geronimo.timer.ThreadPooledTimer;
import org.apache.geronimo.timer.TransactionalExecutorTaskFactory;
import org.apache.geronimo.transaction.context.TransactionContextManager;

/**
 *
 *
 * @version $Revision: 1.2 $ $Date: 2004/07/20 23:36:53 $
 *
 * */
public class VMStoreThreadPooledTransactionalTimer extends ThreadPooledTimer {

    public VMStoreThreadPooledTransactionalTimer(int repeatCount,
            TransactionContextManager transactionContextManager,
            Executor threadPool) {
        super(new TransactionalExecutorTaskFactory(transactionContextManager, repeatCount),
                new VMWorkerPersistence(), threadPool);
    }


    public static final GBeanInfo GBEAN_INFO;

    static {
        GBeanInfoFactory infoFactory = new GBeanInfoFactory(VMStoreThreadPooledTransactionalTimer.class);
        infoFactory.addInterface(PersistentTimer.class);

        infoFactory.addAttribute("repeatCount", int.class, true);
        infoFactory.addReference("TransactionContextManager", TransactionContextManager.class);
        infoFactory.addReference("ThreadPool", Executor.class);

        infoFactory.setConstructor(new String[] {"repeatCount", "TransactionContextManager", "ThreadPool"});
        GBEAN_INFO = infoFactory.getBeanInfo();
    }

    public static GBeanInfo getGBeanInfo() {
        return GBEAN_INFO;
    }
}
