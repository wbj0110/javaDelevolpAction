package com.jdon.jserver.connector.tcp;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import com.jdon.util.Debug;

/**
 * TCP���ݰ����ܴ�����
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
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

        //ͬʱ��SelectionKey���Ϊ�ɶ����Ա��ȡ��
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();

        sk.attach(new TCPHandler(sk, sc));
      }

    } catch (Exception ex) {
      Debug.logVerbose("accept stop!" + ex);
    }
  }

}