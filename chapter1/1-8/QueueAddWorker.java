package com.jdon.jserver.connector.data;

import java.io.*;

import com.jdon.jserver.connector.queue.MessageQueue;

/**
 * һ�������߾�������
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
 * @author banq
 * @version 1.0
 */
public class QueueAddWorker implements QueueWorker{

    private MessageQueue messageQueue = null;

    public QueueAddWorker(MessageQueue messageQueue){
           this.messageQueue = messageQueue;
    }

    public void run(int msgType, Linkable object) throws Exception {
      OutputStream outputStream = object.getOutputStream();
      if (msgType == REQUEST) {
        messageQueue.pushRequest(outputStream);
      } else if (msgType == RESPONSE) {
        messageQueue.pushResponse(outputStream);
      }
    }

}