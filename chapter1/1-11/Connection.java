package com.jdon.jserver.application;

import com.jdon.jserver.connector.queue.MessageQueue;
import com.jdon.jserver.connector.data.*;
import com.jdon.jserver.application.connection.ConnectionFactory;

import com.jdon.util.Debug;

/**
 * 连接接口,需要发送和接受数据时，使用本类。
 * 从ConnectionFactory获得本类实例。
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */
public abstract class Connection {
  private final static String module = Connection.class.getName();

  /**
   * 连接Queue
   */
  protected MessageQueue queue = null;
  /**
   * 服务器端还是客户端连接模式
   */
  protected int CSType;

  /**
   * 写入Object
   * @param obj
   * @throws java.lang.Exception
   */
  public void writeObject(Object obj) throws Exception {
    if (!isConnect())
      throw new Exception("not connected");
    try {
      QueueWorker worker = new QueueAddWorker(queue);
      ObjectType ot = new ObjectType(getWriteMsgType());
      ot.setContent(obj);
      ot.accpet(worker);
    } catch (Exception ex) {
      Debug.logError("TcpConnection writeObject error:" + ex, module);
      throw new Exception(ex);
    }
  }
  /**
   * 读取Object
   * @return Object
   * @throws java.lang.Exception
   */
  public Object readObject() throws Exception {
    if (!isConnect())
      throw new Exception("not connected");
    try {
      QueueWorker worker = new QueueTakeWorker(queue);
      ObjectType ot = new ObjectType(getReadMsgType());
      ot.accpet(worker);
      return ot.getContent();
    } catch (Exception ex) {
      Debug.logError("TcpConnection writeObject error:" + ex, module);
      throw new Exception(ex);
    }
  }

  /**
   * 写入字符串
   * @param msg
   * @throws java.lang.Exception
   */
  public void writeString(String msg) throws Exception {
    if (!isConnect())
      throw new Exception("not connected");
    try {
      QueueWorker worker = new QueueAddWorker(queue);
      StringType st = new StringType(getWriteMsgType());
      st.setContent(msg);
      st.accpet(worker);
    } catch (Exception ex) {
      Debug.logError("TcpConnection writeString error:" + ex, module);
      throw new Exception(ex);

    }
  }

  /**
   * 读取字符串
   * @return String
   * @throws java.lang.Exception
   */
  public String readString() throws Exception {
    if (!isConnect())
      throw new Exception("not connected");
    try {
      QueueWorker worker = new QueueTakeWorker(queue);
      StringType st = new StringType(getReadMsgType());
      st.accpet(worker);
      return st.getContent();
    } catch (Exception ex) {
      Debug.logError("TcpConnection writeObject error:" + ex, module);
      throw new Exception(ex);
    }
  }

  /**
   * 根据是服务器应用还是客户端应用，设置写入Queue中的信息类型
   * 是request 还是response
   * @return
   */
  public int getWriteMsgType() {
    if (CSType == ConnectionFactory.CLIENT)
      return QueueWorker.REQUEST;
    else
      return QueueWorker.RESPONSE;
  }

  /**
   * 根据是服务器应用还是客户端应用，设置从Queue中读取的信息类型
   * 是request 还是response
   * @return
   */
  public int getReadMsgType() {
    if (CSType == ConnectionFactory.CLIENT)
      return QueueWorker.RESPONSE;
    else
      return QueueWorker.REQUEST;
  }

  /**
   * 打开连接
   * @param url
   * @param port
   * @throws java.lang.Exception
   */
  public abstract void open(String url, int port) throws Exception;

  /**
   * 关闭连接
   * @throws java.lang.Exception
   */
  public abstract void close() throws Exception;

  /**
   * 是否连接
   * @return
   * @throws java.lang.Exception
   */
  public abstract boolean isConnect() throws Exception;

}