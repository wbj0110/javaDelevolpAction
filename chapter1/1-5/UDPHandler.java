package com.jdon.jserver.connector.udp;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.nio.charset.*;
import java.util.*;

import com.jdon.util.Debug;

import com.jdon.jserver.connector.SocketDataHandler;
import com.jdon.jserver.connector.queue.QueueFactory;

/**
 * UDP数据包读和发送处理类
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */
public class UDPHandler implements Runnable {

  private final static String module = UDPHandler.class.getName();

  private SocketDataHandler socketDataHandler;

  private final DatagramChannel datagramChannel;
  private final SelectionKey key;

  private static final int READING = 0, SENDING = 1;
  private int state = READING;

  private SocketAddress address = null;

  public UDPHandler(SelectionKey key, DatagramChannel datagramChannel) throws
      IOException {
    socketDataHandler = new SocketDataHandler(QueueFactory.UDP_QUEUE);
    this.datagramChannel = datagramChannel;
    this.key = key;

    Debug.logVerbose(" UDPHandler prepare ...", module);
  }

  public void run() {

    Debug.logVerbose(" UDPHandler running ...", module);
    try {
      if (state == READING)
        read();
      else if (state == SENDING)
        send();
    } catch (Exception ex) {
      Debug.logError("Run  error:" + ex, module);

    }
  }

  private void read() {
    try {
      byte[] array = socketDataHandler.getByte();
      ByteBuffer buffer = ByteBuffer.wrap(array);
      address = datagramChannel.receive(buffer);
      socketDataHandler.receiveRequest(array);

      state = SENDING;
      key.interestOps(SelectionKey.OP_WRITE);

    } catch (Exception ex) {
      Debug.logError("readRequest .. error:" + ex, module);
    }
  }

  private void send(){
    try {
      byte[] response = socketDataHandler.sendResponse();
      ByteBuffer buffer1 = ByteBuffer.wrap(response);
      datagramChannel.send(buffer1, address);

      state = READING;
      key.interestOps(SelectionKey.OP_READ);

    } catch (Exception ex) {
      Debug.logError("readRequest .. error:" + ex, module);
    }

  }


}