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

package javax.mail.internet;
/**
 * @version $Revision: 1.3 $ $Date: 2004/03/10 10:00:28 $
 */
public class AddressException extends ParseException {
    protected int pos;
    protected String ref;
    public AddressException() {
        this(null);
    }
    public AddressException(String message) {
        this(message, null);
    }
    public AddressException(String message, String ref) {
        this(message, null, -1);
    }
    public AddressException(String message, String ref, int pos) {
        super(message);
        this.ref = ref;
        this.pos = pos;
    }
    public String getRef() {
        return ref;
    }
    public int getPos() {
        return pos;
    }
    public String toString() {
        return super.toString() + " (" + ref + "," + pos + ")";
    }
}
