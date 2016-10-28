package com.jdon.jserver.connector;

import com.jdon.util.Debug;
import java.io.*;

import com.jdon.jserver.connector.queue.QueueFactory;
import com.jdon.jserver.connector.queue.MessageQueue;
import com.jdon.jserver.connector.data.*;
import com.jdon.jserver.connector.WrapFactory;

/**
 * 本类是供Socket I/O调用，以实现信号的发送和收取
 * 是MessageQueue与Socket I/O的接口部分
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */
public class SocketDataHandler {
  private final static String module = SocketDataHandler.class.getName();

  private final static QueueFactory queueFactory = QueueFactory.getInstance();
  private final static WrapFactory wrapFactory = WrapFactory.getInstance();

  private final static int DATA_LENGTH = 1024;
  private int queueType;

  public SocketDataHandler(int queueType){
     this.queueType = queueType;
  }

  public byte[] getByte() {
    return new byte[DATA_LENGTH];
  }

  /**
   * 从Queue中取出Request 并实行协议包装
   */
  public byte[] sendRequest() throws Exception{
    byte[] request = null;
    try {
      MessageQueue messageQueue = queueFactory.getQueue(queueType);
      ByteArrayOutputStream outByte = (ByteArrayOutputStream) messageQueue.
          removeReqFirst();
      request = wrapFactory.getRequest(outByte.toByteArray());
    } catch (Exception ex) {
      Debug.logError("sendRequest() error:" + ex, module);
      throw new Exception(ex);
    }
    return request;
  }

  /**
   * 将Request去除协议解码，正文内容保存到Queue中，
   */
  public void receiveRequest(byte[] array) throws Exception{
    try {
      MessageQueue messageQueue = queueFactory.getQueue(queueType);
      byte[] content = wrapFactory.getContentFromRequest(array);
      InputStream bin = new ByteArrayInputStream(content);
      messageQueue.pushRequest(bin);
    } catch (Exception ex) {
      Debug.logError("receiveRequest error:" + ex, module);
      throw new Exception(ex);
    }
  }

  /**
   *  从Queue中取出Response 并实行协议包装
   */
  public byte[] sendResponse() throws Exception{
    byte[] response = null;
    try {
      MessageQueue messageQueue = queueFactory.getQueue(queueType);
      ByteArrayOutputStream bout = (ByteArrayOutputStream) messageQueue.
          removeResFirst();
      response = wrapFactory.getResponse(bout.toByteArray());

    } catch (Exception ex) {
      Debug.logError("sendResponse() error:" + ex, module);
      throw new Exception(ex);
    }
    return response;
  }

  /**
   * 将Response去除协议解码，正文内容保存到Queue中，
   * @param array
   */
  public void receiveResponse(byte[] array) throws Exception{
    try {
      MessageQueue messageQueue = queueFactory.getQueue(queueType);
      byte[] response = wrapFactory.getContentFromResponse(array);
      InputStream bin = new ByteArrayInputStream(response);
      messageQueue.pushResponse(bin);

    } catch (Exception ex) {
      Debug.logError("receiveResponse error:" + ex, module);
      throw new Exception(ex);
    }
  }

}