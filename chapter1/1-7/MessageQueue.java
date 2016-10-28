package com.jdon.jserver.connector.queue;

import java.util.*;
import java.nio.*;
import java.nio.channels.*;

import com.jdon.util.Debug;
import com.jdon.jserver.util.CollectionFactory;

/**
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
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
      // 如果没有任务，就锁定在这里
      while (requestList.isEmpty()) {
        try {
          requestList.wait(); //等待解锁
        } catch (InterruptedException ie) {
          ie.printStackTrace();
        }
      }
      return requestList.removeFirst();
    }
  }

  public Object removeResFirst() {
    synchronized (responseList) {
      // 如果没有任务，就锁定在这里
      while (responseList.isEmpty()) {
        try {
          responseList.wait(); //等待解锁
        } catch (InterruptedException ie) {
          ie.printStackTrace();
        }
      }
      return responseList.removeFirst();
    }
  }

}