package com.jdon.jserver.application.connection;

import com.jdon.jserver.application.Connection;
import com.jdon.util.Debug;
import com.jdon.jserver.connector.tcp.*;
import com.jdon.jserver.connector.udp.*;

/**
 * ���ӹ��������ڻ��Connection����
 *
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
 * @author banq
 * @version 1.0
 */
public class ConnectionFactory {
  private final static String module = ConnectionFactory.class.getName();

  public final static int CLIENT = 1;
  public final static int TCPSERVER = 2;
  public final static int UDPSERVER = 3;

  private TCPClient cclient = null;
  private UDPClient uclient = null;

  private TCPReactor tserver = null;
  private UDPReactor userver = null;

  private int fType; //�Ƿ������˵Ļ���CLient��

  private static ConnectionFactory factory;
  public synchronized static ConnectionFactory getInstance(int fType) {
    if (factory == null)
      factory = new ConnectionFactory(fType);
    return factory;
  }

  private ConnectionFactory(int fType) {
    this.fType = fType;
  }

  /**
   * ���һ��TcpConnectionʵ��
   * @return Connection
   */
  public Connection getTcpConnection(boolean longConnection) {
    if (fType == CLIENT) {
      startTcpClientSocket(longConnection);
      return new TcpConnection(cclient);
    } else {
      startTcpServerSocket();
      return new TcpConnection();
    }
  }

  /**
   * ���һ��UdpConnectionʵ��
   * @return Connection
   */
  public Connection getUdpConnection() {
    if (fType == CLIENT) {
      startUdpClientSocket();
      return new UdpConnection(uclient);
    } else {
      startUdpServerSocket();
      return new UdpConnection();
    }

  }

  /**
   * �����ͻ���Tcp Socket�߳�
   */
  private void startTcpClientSocket(boolean longConnection) {
    if (cclient != null)
      return;
    try {
      cclient = new TCPClient(longConnection);
      Thread thread = new Thread(cclient);
      thread.setDaemon(true);
      thread.start();
      Debug.logVerbose("-->  started Tcp Socket thread ..", module);
    } catch (Exception ex) {
      Debug.logError("started Tcp error:" + ex, module);
    }
  }

  /**
   * �����ͻ���Udp Socket�߳�
   */
  private void startUdpClientSocket() {
    if (uclient != null)
      return;
    try {
      uclient = new UDPClient();
      Thread thread = new Thread(uclient);
      thread.setDaemon(true);
      thread.start();
      Debug.logVerbose("-->  started Udp Socket thread ..", module);
    } catch (Exception ex) {
      Debug.logError("started Udp error:" + ex, module);
    }
  }

  /**
   * ������������TCP Socket�߳�
   */
  private void startTcpServerSocket() {
    if (tserver != null)
      return;
    try {
      ServerCfg cfg = new ServerCfg();
      tserver = new TCPReactor(cfg.getTcpPort());
      Thread thread = new Thread(tserver);
      thread.setDaemon(true);
      thread.start();
      Debug.logVerbose("-->  started Tcp Socket thread ..", module);
    } catch (Exception ex) {
      Debug.logError("started Tcp error:" + ex, module);
    }
  }

  /**
   * ������������UDP Socket�߳�
   */
  private void startUdpServerSocket() {
    if (userver != null)
      return;
    try {
      ServerCfg cfg = new ServerCfg();
      userver = new UDPReactor(cfg.getUdpPort());
      Thread thread = new Thread(userver);
      thread.setDaemon(true);
      thread.start();
      Debug.logVerbose("-->  started Udp Socket thread ..", module);
    } catch (Exception ex) {
      Debug.logError("started Udp error:" + ex, module);
    }
  }

}