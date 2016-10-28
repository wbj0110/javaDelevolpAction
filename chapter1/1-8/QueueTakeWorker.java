package com.jdon.jserver.connector.data;

import java.io.*;

import com.jdon.jserver.connector.queue.MessageQueue;

/**
 * 一个访问者具体子类
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */

public class QueueTakeWorker implements QueueWorker {
  private MessageQueue messageQueue = null;

  public QueueTakeWorker(MessageQueue messageQueue) {
    this.messageQueue = messageQueue;
  }

  public void run(int msgType, Linkable object) throws Exception {

    InputStream bin = null;

    OutputStream outputStream = object.getOutputStream();
    if (msgType == REQUEST) {
      bin = (InputStream) messageQueue.removeReqFirst();
    } else if (msgType == RESPONSE) {
      bin = (InputStream) messageQueue.removeResFirst();

    }
    object.setInputStream(bin);
    bin.close();

  }

}