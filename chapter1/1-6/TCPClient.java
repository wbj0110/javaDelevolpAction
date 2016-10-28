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
 * TCP�ͻ��˳���
 * ��������˷���TCP���ݰ������ܷ���������Ӧ��TCP���ݰ�
 *
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
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
      channel.connect(socketAddress); //��socketAddress
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

      if (key.isConnectable()) { //���ӳɹ�
        SocketChannel keyChannel = (SocketChannel) key.channel();
        if (keyChannel.isConnectionPending()) {
          keyChannel.finishConnect();
        }
        Debug.logVerbose(" connected the server", module);
        sendRequest(key);
      } else if (key.isReadable()) { //������Դӷ�������ȡresponse����

        receiveResponse(key);

        if (!longConnection) close();

      } else if (key.isWritable()) { //������������������request����

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