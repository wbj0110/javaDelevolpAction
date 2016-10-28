package com.jdon.jserver.application.chat;

import java.io.*;
import java.net.*;

import com.jdon.jserver.application.connection.ConnectionFactory;
import com.jdon.jserver.application.Connection;
import com.jdon.util.Debug;

public class ChatClient implements Runnable {
  private final static String module = ChatClient.class.getName();
  private ConnectionFactory connFactory = ConnectionFactory.getInstance(
      ConnectionFactory.CLIENT);

  public void run() {
    try {
      String url = "218.18.144.56";
      for (int i = 200; i < 10000; i++) {

        int port = 81;
//        TcpConnect(url, port);
        UdpConnect(url, i);
      }
    } catch (Exception ex) {
      Debug.logError(ex, module);
    }

  }

  public void TcpConnect(String url, int port) throws Exception {
    Debug.logVerbose(" --> begin to send TCP", module);
    Connection conn = connFactory.getTcpConnection(false);
    conn.open(url, port);
    for (int i = 0; i < 1; i++) {

      String msg = i + "helloI �� am Peng \r\n" + "this is two line" + i;
      conn.writeString(msg);

      Debug.logVerbose("send TCP msg=" + msg, module);
    }
    System.out.println(" send TCP ok ................");

    for (int i = 0; i < 1; i++) {
      String result = conn.readString();
      System.out.println(" TCP Response result =" + result);
    }
    conn.close();
  }

  public void UdpConnect(String url, int port) throws Exception {
    Debug.logVerbose(" --> begin to send UDP", module);
    Connection conn = connFactory.getUdpConnection();
    conn.open(url, port);
    for (int i = 5; i < 8; i++) {

      String msg = i + "helloI �� am Peng \r\n" + "this is two line" + i;
      conn.writeString(msg);

      Debug.logVerbose("send UDP msg=" + msg, module);
    }
    System.out.println(" send UDP ok ....................");

    for (int i = 5; i < 8; i++) {
      String result = conn.readString();
      System.out.println("get UDP Response result =" + result);
    }
    conn.close();
  }

  public static void main(String[] args) {
    Thread chat = new Thread(new ChatClient());
    chat.start();

  }

}