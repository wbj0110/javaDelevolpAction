package com.jdon.jserver.connector.tcp;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import com.jdon.util.Debug;

/**
 * TCP包接受核心功能类
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */
public class TCPReactor implements Runnable {

  private final static String module = TCPReactor.class.getName();

  private final Selector selector;
  private final ServerSocketChannel ssc;

  public TCPReactor(int port) throws IOException {

    selector = Selector.open();
    ssc = ServerSocketChannel.open();

    InetSocketAddress address =
        new InetSocketAddress(InetAddress.getLocalHost(), port);
    ssc.socket().bind(address);
    Debug.logVerbose("-->Start host:" + InetAddress.getLocalHost() + " port=" +
                     port);

    ssc.configureBlocking(false);
    //向selector注册该channel
    SelectionKey sk = ssc.register(selector, SelectionKey.OP_ACCEPT);

    //利用sk的attache功能绑定Acceptor 如果有事情，触发Acceptor
    sk.attach(new Acceptor(selector, ssc));

    Debug.logVerbose("-->TCP Server started");
  }

  public void run() {

    while (true) {
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
        Debug.logError("reactor error" + ex, module);
      }

    }
  }

  //运行Acceptor或SocketReadHandler
  private void dispatch(SelectionKey key) {
    Runnable r = (Runnable) (key.attachment());
    if (r != null) {

      r.run();
    }
  }
}
