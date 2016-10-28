package com.jdon.jserver.application;

import com.jdon.jserver.connector.queue.MessageQueue;
import com.jdon.jserver.connector.data.*;
import com.jdon.jserver.application.connection.ConnectionFactory;

import com.jdon.util.Debug;

/**
 * ���ӽӿ�,��Ҫ���ͺͽ�������ʱ��ʹ�ñ��ࡣ
 * ��ConnectionFactory��ñ���ʵ����
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
 * @author banq
 * @version 1.0
 */
public abstract class Connection {
  private final static String module = Connection.class.getName();

  /**
   * ����Queue
   */
  protected MessageQueue queue = null;
  /**
   * �������˻��ǿͻ�������ģʽ
   */
  protected int CSType;

  /**
   * д��Object
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
   * ��ȡObject
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
   * д���ַ���
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
   * ��ȡ�ַ���
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
   * �����Ƿ�����Ӧ�û��ǿͻ���Ӧ�ã�����д��Queue�е���Ϣ����
   * ��request ����response
   * @return
   */
  public int getWriteMsgType() {
    if (CSType == ConnectionFactory.CLIENT)
      return QueueWorker.REQUEST;
    else
      return QueueWorker.RESPONSE;
  }

  /**
   * �����Ƿ�����Ӧ�û��ǿͻ���Ӧ�ã����ô�Queue�ж�ȡ����Ϣ����
   * ��request ����response
   * @return
   */
  public int getReadMsgType() {
    if (CSType == ConnectionFactory.CLIENT)
      return QueueWorker.RESPONSE;
    else
      return QueueWorker.REQUEST;
  }

  /**
   * ������
   * @param url
   * @param port
   * @throws java.lang.Exception
   */
  public abstract void open(String url, int port) throws Exception;

  /**
   * �ر�����
   * @throws java.lang.Exception
   */
  public abstract void close() throws Exception;

  /**
   * �Ƿ�����
   * @return
   * @throws java.lang.Exception
   */
  public abstract boolean isConnect() throws Exception;

}