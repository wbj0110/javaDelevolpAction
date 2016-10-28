package com.jdon.jserver.application.connection;

import java.nio.channels.DatagramChannel;
import com.jdon.jserver.application.Connection;
import com.jdon.jserver.connector.udp.UDPClient;
import com.jdon.jserver.connector.queue.QueueFactory;
import com.jdon.jserver.connector.data.*;

import com.jdon.util.Debug;

/**
 * UDP不需要连接，直接发送和接受，因此发送和接受数据包比TCP快，
 * 但是，容易丢包，在丢包的情况下，需要使用TCP连接重发。
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */
public class UdpConnection extends Connection {
  private final static String module = UdpConnection.class.getName();
  private final static QueueFactory queueFactory = QueueFactory.getInstance();
  private UDPClient client = null;


  /**
   * 客户端连接初始化
   * @param client
   */
  public UdpConnection(UDPClient client){
    this();
    this.client = client;
    this.CSType = ConnectionFactory.CLIENT;
  }

  /**
   * 服务器端连接初始化
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