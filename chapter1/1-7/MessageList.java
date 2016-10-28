package com.jdon.jserver.connector.queue;

import java.util.*;
import java.nio.*;
import java.nio.channels.*;

import com.jdon.util.Debug;

/**
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
 * @author banq
 * @version 1.0
 */
public class MessageList {
  private final static String module = MessageList.class.getName();
  //Request�źŵ�Queue
  private LinkedList requestList = new LinkedList();
  //Response�źŵ�Queue
  private LinkedList responseList = new LinkedList();
  //ʹ�õ�̬ģʽ��֤��ǰJVM��ֻ��һ��MessageListʵ��
  private static MessageList messageList =  new MessageList();
  public static MessageList getInstance(){
     return messageList;
  }
  //��������
  public void pushRequest(Message requestMsg) {
    synchronized (requestList) {
      requestList.add(requestMsg);
      requestList.notifyAll();  //��������requestList�������߳�
    }
  }
  //ȡ��Queue�е�һ����
  public synchronized Message removeReqFirst() {
    synchronized (requestList) {
      // ���û�����ݣ�������������
      while (requestList.isEmpty()) {
        try {
          requestList.wait(); //�ȴ����� �ȴ��������ݺ������
        } catch (InterruptedException ie) {}
      }
      return (Message) requestList.removeFirst();
    }
  }
}
