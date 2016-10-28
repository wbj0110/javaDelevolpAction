package com.jdon.jserver.connector.udp;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import com.jdon.util.Debug;

/**
 * UDP数据包接受
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */
public class UDPReactor implements Runnable {

  private final static String module = UDPReactor.class.getName();

  private final Selector selector;

  public UDPReactor(int port) throws IOException {

    selector = Selector.open();
    InetSocketAddress address =
        new InetSocketAddress(InetAddress.getLocalHost(), port);

    DatagramChannel channelRec = openDatagramChannel();
    channelRec.socket().bind(address); //绑定socketAddress

    //向selector注册该channel
    SelectionKey key = channelRec.register(selector, SelectionKey.OP_READ);
    key.attach(new UDPHandler(key, channelRec));

    Debug.logVerbose("-->Start host:" + InetAddress.getLocalHost() + " port=" +
                     port);

    Debug.logVerbose("-->UDP Server started");

  }

  private DatagramChannel openDatagramChannel() {
    DatagramChannel channel = null;
    try {
      channel = DatagramChannel.open();
      channel.configureBlocking(false);

    } catch (Exception e) {
      Debug.logError(e, module);
    }
    return channel;
  }

  public void run() { // normally in a new Thread
    while (!Thread.interrupted()) {
      try {
        selector.select();
        Set selected = selector.selectedKeys();
        Iterator it = selected.iterator();
        //Selector如果发现channel有OP_ACCEPT或READ事件发生，下列遍历就会进行。
        while (it.hasNext())

          //来一个事件 第一次触发一个accepter线程
          //以后触发SocketReadHandler
          dispatch( (SelectionKey) (it.next()));
        selected.clear();
      } catch (IOException ex) {
        Debug.logError("reactor error!" + ex, module);
      }
    }
  }

  //运行Acceptor或SocketReadHandler
  private void dispatch(SelectionKey k) {
    Runnable r = (Runnable) (k.attachment());
    if (r != null) {

      r.run();

    }
  }

}