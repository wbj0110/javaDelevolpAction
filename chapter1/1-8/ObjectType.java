package com.jdon.jserver.connector.data;

import java.nio.*;
import java.io.*;
import java.net.*;

import com.jdon.util.Debug;

/**
 * Visitable具体子类
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */

public class ObjectType implements Linkable {

  private Object content = null;
  private int msgType;
  private ByteBuffer byteBuffer = null;

  public ObjectType(int msgType) {
    this.msgType = msgType;
  }

  public Object getContent() {
    return content;
  }
  public void setContent(Object content){
     this.content = content;
  }

  public void accpet(QueueWorker worker) throws Exception {
    worker.run(msgType, this);
  }

  public OutputStream getOutputStream() {
    OutputStream outputStream = null;
    try {
      outputStream = DataTypeHelper.writeObject(content);
    } catch (Exception ex) {
    }
    return outputStream;
  }

  public void setInputStream(InputStream in) {
    try {
      this.content = DataTypeHelper.getObject(in);
    } catch (Exception ex) {
    }
  }

}
