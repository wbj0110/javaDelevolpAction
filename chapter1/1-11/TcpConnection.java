package com.jdon.jserver.application.connection;

import java.nio.channels.SocketChannel;

import com.jdon.jserver.application.Connection;
import com.jdon.jserver.connector.tcp.TCPClient;
import com.jdon.jserver.connector.queue.QueueFactory;
import com.jdon.jserver.connector.data.*;

import com.jdon.util.Debug;

/**
 * TCP���ӣ�TCP���ӱȽ��ȶ���û�ж����������ٶȱ�UDP�͡�
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
 * @author banq
 * @version 1.0
 */
public class TcpConnection extends Connection {
  private final static String module = TcpConnection.class.getName();
  private final static QueueFactory queueFactory = QueueFactory.getInstance();

  private TCPClient client = null;
  private SocketChannel sc = null;
  private boolean isConnect = false;

  /**
   * �ͻ������ӳ�ʼ��
   * @param client
   */
  public TcpConnection(TCPClient client){
    this();
    this.client = client;
    this.CSType = ConnectionFactory.CLIENT;
  }

  /**
   * �����������ӳ�ʼ��
   */
  public TcpConnection(){
     queue = queueFactory.getQueue(QueueFactory.TCP_QUEUE);
     this.CSType = ConnectionFactory.TCPSERVER;
     isConnect = true;
  }


  public void open(String url, int port) throws Exception {
    try {
      client.openSocketChannel(url, port);
      isConnect = true;
    } catch (Exception ex) {
      Debug.logError("TcpConnection open error:" + ex, module);
      throw new Exception(ex);
    }
  }

  public boolean isConnect() throws Exception{
    return isConnect;
  }

  public void close() throws Exception {
    if (!isConnect) return;
    try {
      client.close();
      isConnect = false;;
    } catch (Exception ex) {
      Debug.logError("TcpConnection close error:" + ex, module);
      throw new Exception(ex);
    }

  }

}