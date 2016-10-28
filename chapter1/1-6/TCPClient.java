package com.jdon.jserver.connector.tcp;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import com.jdon.util.Debug;

import com.jdon.jserver.connector.SocketDataHandler;
import com.jdon.jserver.connector.queue.QueueFactory;

/**
 * TCP客户端程序
 * 向服务器端发送TCP数据包，接受服务器端响应的TCP数据包
 *
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */
public class TCPClient implements Runnable {
  private final static String module = TCPClient.class.getName();

  private SocketDataHandler socketDataHandler;
  private Selector selector;
  private volatile SocketChannel channel;

  private volatile boolean longConnection;
  private Thread runThread;

  public TCPClient(boolean longConnection) {
    try {
      socketDataHandler = new SocketDataHandler(QueueFactory.TCP_QUEUE);
      selector = Selector.open();
      this.longConnection = longConnection;
    } catch (Exception e) {
      Debug.logError("init error:" + e, module);
    }
  }

  public void openSocketChannel(String url, int port) throws Exception {
    try {
      channel = SocketChannel.open();
      channel.configureBlocking(false);

      InetSocketAddress socketAddress = new InetSocketAddress(url, port);
      channel.connect(socketAddress); //绑定socketAddress
      Debug.logVerbose("connecting to server:" + url + ":" + port, module);

      channel.register(selector, SelectionKey.OP_CONNECT);

    } catch (Exception e) {
      Debug.logError(e, module);
      throw new Exception(e);
    }

  }

  public void run() {
    try {
      while (true) {
        if (selector.select(30) > 0) {
          doSelector(selector);
        }
      }
    } catch (Exception e) {
      Debug.logError("run error:" + e, module);

    }
  }

  private void doSelector(Selector selector) throws Exception {
    Set readyKeys = selector.selectedKeys();
    Iterator readyItor = readyKeys.iterator();
    while (readyItor.hasNext()) {
      SelectionKey key = (SelectionKey) readyItor.next();
      readyItor.remove();

      doKey(key);
      readyKeys.clear();
    }
  }

  private void doKey(SelectionKey key) {
    try {

      if (key.isConnectable()) { //连接成功
        SocketChannel keyChannel = (SocketChannel) key.channel();
        if (keyChannel.isConnectionPending()) {
          keyChannel.finishConnect();
        }
        Debug.logVerbose(" connected the server", module);
        sendRequest(key);
      } else if (key.isReadable()) { //如果可以从服务器读取response数据

        receiveResponse(key);

        if (!longConnection) close();

      } else if (key.isWritable()) { //如果可以向服务器发送request数据

        sendRequest(key);
      }
    } catch (Exception e) {
      Debug.logError("run error:" + e, module);
    }
  }

  private void sendRequest(SelectionKey key) {
    try {
      byte[] request = socketDataHandler.sendRequest();

      ByteBuffer buffer = ByteBuffer.wrap(request);

      SocketChannel keyChannel = (SocketChannel) key.channel();
      Debug.logVerbose("-->begin to send request" + keyChannel.toString(),
                       module);
      keyChannel.write(buffer);
      Debug.logVerbose("--> sent request", module);

      key.interestOps(SelectionKey.OP_READ);
      selector.wakeup();

    } catch (Exception ex) {
      Debug.logError(ex, module);
    }
  }

  private void receiveResponse(SelectionKey key) {
    try {

      byte[] array = socketDataHandler.getByte();
      ByteBuffer buffer = ByteBuffer.wrap(array);

      SocketChannel keyChannel = (SocketChannel) key.channel();
      keyChannel.read(buffer);

      socketDataHandler.receiveResponse(array);

      key.interestOps(SelectionKey.OP_WRITE);
      selector.wakeup();
    } catch (Exception ex) {
      Debug.logError(ex, module);
    }
  }

  public void close() {
    if (channel != null) {
      try {
        SelectionKey key = channel.keyFor(selector);
        key.cancel();
        channel.close();
      } catch (Exception ignored) {
      }
    }
  }

}