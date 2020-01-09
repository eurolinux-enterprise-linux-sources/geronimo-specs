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

package org.apache.geronimo.remoting.transport.async.nio;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Properties;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.geronimo.proxy.SimpleComponent;
import org.apache.geronimo.remoting.transport.ConnectionFailedException;
import org.apache.geronimo.remoting.transport.TransportException;
import org.apache.geronimo.remoting.transport.URISupport;
import org.apache.geronimo.remoting.transport.async.AsyncMsg;
import org.apache.geronimo.remoting.transport.async.Channel;
import org.apache.geronimo.remoting.transport.async.ChannelListner;

import EDU.oswego.cs.dl.util.concurrent.Mutex;
/**
 * The Blocking implementation of the AsynchChannel interface.  
 * 
 * This implemenation uses the standard Java 1.3 blocking socket IO.
 *
 * @version $Revision: 1.3 $ $Date: 2004/03/10 09:59:20 $
 */
public class NonBlockingChannel extends SimpleComponent implements Channel, SelectionEventListner {

    static final private Log log = LogFactory.getLog(NonBlockingChannel.class);

    private ChannelListner listner;
    private Thread worker;
    private SocketChannel socketChannel;
    private URI remoteURI;
    private boolean closing = false;

    private Inflater inflator;
    private Deflater deflater;

    private SelectorManager selectorManager;
    private SelectionKey selectionKey;

    private URI requestedURI;

    public void open(URI remoteURI, URI backConnectURI, ChannelListner listner) throws TransportException {

        if (log.isTraceEnabled())
            log.trace("Connecting to : " + remoteURI);

        this.listner = listner;
        this.remoteURI = remoteURI;
        int port = remoteURI.getPort();
        boolean enableTcpNoDelay = true;

        Properties params = URISupport.parseQueryParameters(remoteURI);
        enableTcpNoDelay = params.getProperty("tcp.nodelay", "true").equals("true");
        int compression = Integer.parseInt(params.getProperty("compression", "-1"));

        try {
            InetAddress addr = InetAddress.getByName(remoteURI.getHost());
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            socketChannel.connect(new InetSocketAddress(addr, port));
        } catch (Exception e) {
            throw new ConnectionFailedException("" + e);
        }
        try {
            socketChannel.socket().setTcpNoDelay(enableTcpNoDelay);

            DataOutputStream out = new DataOutputStream(socketChannel.socket().getOutputStream());
            out.writeUTF(remoteURI.toString());
            out.writeUTF(backConnectURI.toString());
            /*
            if (Registry.instance.getServerForClientRequest() == null)
                out.writeUTF("async://" + socketChannel.socket().getLocalAddress().getHostAddress() + ":0");
            else
                out.writeUTF(Registry.instance.getServerForClientRequest().getClientConnectURI().toString());
            */
            out.flush();

            if (compression != -1) {
                inflator = new Inflater(true);
                deflater = new Deflater(compression, true);
            }

            // Setup the selector            
            socketChannel.configureBlocking(false); // Make the connect be non-blocking.
            selectorManager = SelectorManager.getInstance();
            selectorManager.start();
            selectionKey = selectorManager.register( socketChannel, SelectionKey.OP_READ, this);

        } catch (Exception e) {
            throw new TransportException("Connection handshake failed: " + e);
        }

    }

    public void init(URI localURI, SocketChannel socketChannel) throws IOException, URISyntaxException {
        this.socketChannel = socketChannel;

        DataOutputStream out = new DataOutputStream(socketChannel.socket().getOutputStream());
        out.flush();

        DataInputStream in = new DataInputStream(socketChannel.socket().getInputStream());
        // Use to get connection options.
        String destURI = in.readUTF();
        // Use in case we need to establish new connections back to 
        // the source vm.  Could be null.
        String sourceURI = in.readUTF();
        this.remoteURI = new URI(sourceURI);
        this.requestedURI = new URI(destURI);
        if (log.isTraceEnabled()) {
            log.trace("Remote URI    : " + remoteURI);
            log.trace("Requested URI : " + requestedURI);
        }

        // What options did the client want to use with this connection?		
        boolean enableTcpNoDelay = true;
        Properties params = URISupport.parseQueryParameters(requestedURI);
        enableTcpNoDelay = params.getProperty("tcp.nodelay", "true").equals("true");
        int compression = Integer.parseInt((String) params.getProperty("compression", "-1"));

        if (compression != -1) {
            inflator = new Inflater(true);
            deflater = new Deflater(compression, true);
        }

        /*
        */
        socketChannel.socket().setTcpNoDelay(enableTcpNoDelay);
        if (log.isTraceEnabled()) {
            log.trace("Compression level : " + compression);
            log.trace("tcp no delay : " + enableTcpNoDelay);
        }
    }

    public void open(ChannelListner listner) throws TransportException {
        try {
            this.listner = listner;
            
            // Setup the selector            
            socketChannel.configureBlocking(false); // Make the connect be non-blocking.
            selectorManager = SelectorManager.getInstance();
            selectorManager.start();
            selectionKey = selectorManager.register( socketChannel, SelectionKey.OP_READ, this);
            
        } catch (Exception e) {
            throw new TransportException("Connection handshake failed: " + e);
        }
    }

    static int nextId = 0;
    /**
     * @return
     */
    synchronized private int getNextID() {
        return nextId++;
    }

    /**
     * @param data
     * @return
     */
    private ByteBuffer[] serialize(AsyncMsg data) throws IOException {
        ByteBuffer rc[] = new ByteBuffer[2];
        rc[0] = ByteBuffer.allocate(4);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream t = baos;
        if (deflater != null)
            t = new DeflaterOutputStream(t, deflater);
        DataOutputStream out = new DataOutputStream(t);

        data.writeExternal(out);
        out.close();
        rc[1] = ByteBuffer.wrap(baos.toByteArray());
        rc[0].putInt(rc[1].limit());

        rc[0].rewind();
        rc[1].rewind();
        return rc;
    }

    /**
     * @param buffer
     */
    public AsyncMsg deserialize(ByteBuffer[] message) throws IOException {
        AsyncMsg asyncMsg = new AsyncMsg();

        InputStream t = new ByteArrayInputStream(message[1].array());
        if (inflator != null)
            t = new InflaterInputStream(t, inflator);
        DataInputStream in = new DataInputStream(t);

        asyncMsg.readExternal(in);
        in.close();
        return asyncMsg;
    }

    /**
     * Starts to terminate the connection.  Lets the remote end
     * know that we are closing.
     * 
     * The server side calls this close.  Could be called in response to
     * 2 events:
     * - we initiated the close() (so we finish the close)
     * - An asynch error initiated the close(). (so we start the close process)
     * We keep state to know if we started the socket close().  
     */
    synchronized private void asyncClose() {
        // socket is null when we finish close()
        if (socketChannel == null)
            return;
        try {
            socketChannel.socket().shutdownInput();
            // were we allready closing??		
            if (closing) {
                // both side should be shutdown now.  finish close.
                forcedClose();            
            } else {
                closing = true;
                listner.closeEvent();
            }
            
            
        } catch (IOException e) {
            // If the 'nice' shutdown fails at any point,
            // then do the forced shutdown.
            forcedClose();
        }
    }

    /**
     * Starts to terminate the connection.  Lets the remote end
     * know that we are closing.
     * 
     * The client side calls this close.  Could be called in response to
     * 2 events:
     * - the remote sever initiated the close(). (so we finish the close)
     * - we initiated the close() (so we wait for the remote side to finish the close)
     * We keep state to know if we started the socket close().  
     *   
     */
    synchronized public void close() {
        // socket is null when we finish close()
        if (socketChannel == null)
            return;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.asIntBuffer().put(-1);
            synchronized (sendMutex) {
                socketChannel.write(buffer);
                socketChannel.socket().shutdownOutput();
            }
            // were we allready closing??		
            if (closing) {
                // both side should be shutdown now.  finish close.
                forcedClose();            
            } else {
                closing = true;
            }
        } catch (IOException e) {
            forcedClose();
        }
    }

    /**
     * forcibly terminates the connection without telling the remote end 
     * that the connection is being closed. 
     */
    private void forcedClose() {
        if( socketChannel == null )
            return;
        try {
            selectionKey.cancel();
            socketChannel.close();
            socketChannel = null;
            SelectorManager.getInstance().stop();            
        } catch (Throwable e) {
        }
        socketChannel = null;
    }

    /**
     * @return
     */
    public URI getRemoteURI() {
        return remoteURI;
    }

    synchronized public void selectionEvent(SelectionKey selection) {
        if (selection.isWritable())
            serviceWrite();
        if (selection.isReadable())
            serviceRead();
    }


    Mutex sendMutex = new Mutex();

    /**
     */
    public void send(AsyncMsg data) throws TransportException {
        try {
            ByteBuffer buffers[] = serialize(data);
            
            if( !sendMutex.attempt(10000) )
                throw new TransportException("Send timeout.");
            if (closing)
                throw new TransportException("connection has been closed.");
            sendBuffer = buffers;
            
            flushSendBuffer();
            
        } catch (IOException e) {
            throw new TransportException("" + e);
        } catch (InterruptedException e) {
            throw new TransportException("" + e);
        }
    }


    /**
     * 
     */
    private void flushSendBuffer() throws IOException {
        socketChannel.write(sendBuffer);
        if ( sendBuffer[1].hasRemaining()  ) {
            // not all was delivered in this call setup selector
            // so we setup to finish sending async.            
            selectorManager.setInterestOps(selectionKey, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            
        } else {
            // We are done writing.
            selectorManager.setInterestOps(selectionKey, SelectionKey.OP_READ);
            sendMutex.release();
        }
    }

    ByteBuffer receiveBuffer[] = new ByteBuffer[] { ByteBuffer.allocate(4), ByteBuffer.allocate(1024 * 10)};
    ByteBuffer sendBuffer[] = new ByteBuffer[] { ByteBuffer.allocate(4), ByteBuffer.allocate(0)};

    private void serviceWrite() {
        try {
            flushSendBuffer();                
        } catch (IOException e) {
            log.debug("Communications error, closing connection: ", e);
            asyncClose();
        }
    }

    private void serviceRead() {

        boolean tracing = log.isTraceEnabled();

        if (tracing)
            log.trace("ReadDataAction triggered.");

        try {
            while (true) {

                // Are we reading the header??
                if (receiveBuffer[0].hasRemaining()) {
                    if (tracing)
                        log.trace("Reading header");

                    socketChannel.read(receiveBuffer[0]);
                    if (receiveBuffer[0].hasRemaining())
                        break; // not done reading the header.

                    receiveBuffer[0].flip();
                    int size = receiveBuffer[0].getInt();

                    // The socket is being closed.
                    if (size == -1)
                        break;

                    // Do we need to incread the capacity of our buffer?                                
                    if (size > receiveBuffer[1].capacity())
                        receiveBuffer[1] = ByteBuffer.allocate(size);

                    receiveBuffer[1].clear();
                    receiveBuffer[1].limit(size);
                }
                // Are we reading the body??
                if (receiveBuffer[1].hasRemaining()) {
                    if (tracing)
                        log.trace("Reading body");

                    socketChannel.read(receiveBuffer[1]);
                    if (receiveBuffer[1].hasRemaining())
                        break; // not done reading the body.

                    receiveBuffer[0].flip();
                    listner.receiveEvent(deserialize(receiveBuffer));
                    receiveBuffer[0].clear();
                }
            }
            if (tracing)
                log.trace("No more data available to be read.");

        } catch (IOException e) {
            log.debug("Communications error, closing connection: ", e);
            asyncClose();
        }
    }

    /**
     * @return
     */
    public URI getRequestedURI() {
        return requestedURI;
    }

}
