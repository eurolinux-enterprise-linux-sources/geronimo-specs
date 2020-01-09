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

package org.apache.geronimo.deployment.util;

import java.io.InputStream;
import java.io.IOException;

/**
 * This class is a workaround for XMLBeans unfortunate habit of closing streams it reads from
 * combined with JarInputStreams insistence that you cant do that.
 *
 * @version $Revision: 1.3 $ $Date: 2004/03/10 09:58:49 $
 *
 * */
public class UnclosableInputStream extends InputStream {

    private final InputStream delegate;

    public UnclosableInputStream(InputStream delegate) {
        this.delegate = delegate;
    }

    public void close() {
    }

    public int available() throws IOException {
        return delegate.available();
    }

    public void mark(int readLimit) {
        delegate.mark(readLimit);
    }

    public boolean markSupported() {
        return delegate.markSupported();
    }

    public int read() throws IOException {
        return delegate.read();
    }

    public int read(byte[] buffer) throws IOException {
        return delegate.read(buffer);
    }

    public int read(byte[] buffer, int offset, int length) throws IOException {
        return delegate.read(buffer, offset, length);
    }

    public void reset() throws IOException {
        delegate.reset();
    }

    public long skip(long n) throws IOException {
        return delegate.skip(n);
    }


}