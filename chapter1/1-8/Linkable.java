package com.jdon.jserver.connector.data;

import java.io.*;

/**
 * Visitorģʽ��Visitable
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
 * @author banq
 * @version 1.0
 */
public interface Linkable {

  public void accpet(QueueWorker worker) throws Exception;

  public OutputStream getOutputStream();

  public void setInputStream(InputStream in);

}