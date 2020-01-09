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

package javax.mail;
/**
 * @version $Revision: 1.3 $ $Date: 2004/03/10 10:00:27 $
 */
public interface UIDFolder {
    public static final long LASTUID = -11;
    public abstract long getUIDValidity() throws MessagingException;
    public abstract Message getMessageByUID(long uid)
        throws MessagingException;
    public abstract Message[] getMessagesByUID(long start, long end)
        throws MessagingException;
    public abstract Message[] getMessagesByUID(long[] ids)
        throws MessagingException;
    public abstract long getUID(Message message) throws MessagingException;
    public static class FetchProfileItem extends FetchProfile.Item {
        public static final FetchProfileItem UID = new FetchProfileItem("Uid");
        protected FetchProfileItem(String name) {
            super(name);
        }
    }
}
