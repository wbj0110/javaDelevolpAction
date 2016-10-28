package com.jdon.jserver.connector.queue;

import java.util.*;
import java.nio.*;
import java.nio.channels.*;

import com.jdon.util.Debug;

/**
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */
public class MessageList {
  private final static String module = MessageList.class.getName();
  //Request信号的Queue
  private LinkedList requestList = new LinkedList();
  //Response信号的Queue
  private LinkedList responseList = new LinkedList();
  //使用单态模式保证当前JVM中只有一个MessageList实例
  private static MessageList messageList =  new MessageList();
  public static MessageList getInstance(){
     return messageList;
  }
  //加入数据
  public void pushRequest(Message requestMsg) {
    synchronized (requestList) {
      requestList.add(requestMsg);
      requestList.notifyAll();  //提醒锁在requestList的其它线程
    }
  }
  //取出Queue中第一数据
  public synchronized Message removeReqFirst() {
    synchronized (requestList) {
      // 如果没有数据，就锁定在这里
      while (requestList.isEmpty()) {
        try {
          requestList.wait(); //等待解锁 等待加入数据后的提醒
        } catch (InterruptedException ie) {}
      }
      return (Message) requestList.removeFirst();
    }
  }
}
