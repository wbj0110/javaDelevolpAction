package com.jdon.jserver.connector.tcp;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import com.jdon.util.Debug;

/**
 * TCP�����ܺ��Ĺ�����
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
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
    //��selectorע���channel
    SelectionKey sk = ssc.register(selector, SelectionKey.OP_ACCEPT);

    //����sk��attache���ܰ�Acceptor ��������飬����Acceptor
    sk.attach(new Acceptor(selector, ssc));

    Debug.logVerbose("-->TCP Server started");
  }

  public void run() {

    while (true) {
      try {

        selector.select();
        Set selected = selector.selectedKeys();
        Iterator it = selected.iterator();
        //Selector�������channel��OP_ACCEPT��READ�¼����������б����ͻ���С�
        while (it.hasNext())

          //��һ���¼� ��һ�δ���һ��accepter�߳�
          //�Ժ󴥷�SocketReadHandler
          dispatch( (SelectionKey) (it.next()));
        selected.clear();
      } catch (IOException ex) {
        Debug.logError("reactor error" + ex, module);
      }

    }
  }

  //����Acceptor��SocketReadHandler
  private void dispatch(SelectionKey key) {
    Runnable r = (Runnable) (key.attachment());
    if (r != null) {

      r.run();
    }
  }
}
