package com.jdon.jserver.connector.data;

/**
 * Visitor模式的Visitor
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */
public interface QueueWorker {

  final static int REQUEST = 1;
  final static int RESPONSE = 2;

  public void run(int msgType, Linkable object) throws Exception;

}