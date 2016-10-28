package com.jdon.jserver.application.connection;

import java.nio.channels.DatagramChannel;
import com.jdon.jserver.application.Connection;
import com.jdon.jserver.connector.udp.UDPClient;
import com.jdon.jserver.connector.queue.QueueFactory;
import com.jdon.jserver.connector.data.*;

import com.jdon.util.Debug;

/**
 * UDP����Ҫ���ӣ�ֱ�ӷ��ͺͽ��ܣ���˷��ͺͽ������ݰ���TCP�죬
 * ���ǣ����׶������ڶ���������£���Ҫʹ��TCP�����ط���
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
 * @author banq
 * @version 1.0
 */
public class UdpConnection extends Connection {
  private final static String module = UdpConnection.class.getName();
  private final static QueueFactory queueFactory = QueueFactory.getInstance();
  private UDPClient client = null;


  /**
   * �ͻ������ӳ�ʼ��
   * @param client
   */
  public UdpConnection(UDPClient client){
    this();
    this.client = client;
    this.CSType = ConnectionFactory.CLIENT;
  }

  /**
   * �����������ӳ�ʼ��
   */
  public UdpConnection(){
    queue = queueFactory.getQueue(QueueFactory.UDP_QUEUE);
    this.CSType = ConnectionFactory.UDPSERVER;
  }


  public boolean isConnect() throws Exception{
    return true;
  }

  public void open(String url, int port) throws Exception {
    try {
      client.openDatagramChannel(url, port);
    } catch (Exception ex) {
      Debug.logError("TcpConnection open error:" + ex, module);
      throw new Exception(ex);
    }
  }

  public void close() throws Exception {
    try {

    } catch (Exception ex) {
      Debug.logError("TcpConnection close error:" + ex, module);
      throw new Exception(ex);
    }

  }

}