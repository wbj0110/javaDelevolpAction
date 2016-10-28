package com.jdon.jserver.connector.queue;

import java.util.*;
import java.nio.*;
import java.nio.channels.*;

import com.jdon.util.Debug;
import com.jdon.jserver.util.CollectionFactory;

/**
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
 * @author banq
 * @version 1.0
 */
public class MessageQueue {

  private final static String module = MessageQueue.class.getName();

  private LinkedList requestList = new LinkedList();
  private LinkedList responseList = new LinkedList();

  public void pushRequest(Object requestMsg) {
    synchronized (requestList) {
      requestList.add(requestMsg);
      requestList.notifyAll();
    }

  }

  public void pushResponse(Object responseMsg) {
    synchronized (responseList) {
      responseList.add(responseMsg);
      responseList.notifyAll();
    }
  }

  public Object removeReqFirst() {
    synchronized (requestList) {
      // ���û�����񣬾�����������
      while (requestList.isEmpty()) {
        try {
          requestList.wait(); //�ȴ�����
        } catch (InterruptedException ie) {
          ie.printStackTrace();
        }
      }
      return requestList.removeFirst();
    }
  }

  public Object removeResFirst() {
    synchronized (responseList) {
      // ���û�����񣬾�����������
      while (responseList.isEmpty()) {
        try {
          responseList.wait(); //�ȴ�����
        } catch (InterruptedException ie) {
          ie.printStackTrace();
        }
      }
      return responseList.removeFirst();
    }
  }

}