package com.jdon.jserver.connector.tcp;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import com.jdon.util.Debug;

/**
 * TCP数据包接受处理类
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */
public class Acceptor implements Runnable {

  private final Selector selector;
  private final ServerSocketChannel ssc;

  public Acceptor(Selector selector, ServerSocketChannel ssc) {
    this.selector = selector;
    this.ssc = ssc;

  }

  public void run() {
    try {
      SocketChannel sc = ssc.accept();
      if (sc != null) {
        sc.configureBlocking(false);
        SelectionKey sk = sc.register(selector, 0);

        //同时将SelectionKey标记为可读，以便读取。
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();

        sk.attach(new TCPHandler(sk, sc));
      }

    } catch (Exception ex) {
      Debug.logVerbose("accept stop!" + ex);
    }
  }

}