package com.jdon.jserver.connector.data;

/**
 * Visitorģʽ��Visitor
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
 * @author banq
 * @version 1.0
 */
public interface QueueWorker {

  final static int REQUEST = 1;
  final static int RESPONSE = 2;

  public void run(int msgType, Linkable object) throws Exception;

}