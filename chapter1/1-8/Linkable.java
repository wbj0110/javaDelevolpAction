package com.jdon.jserver.connector.data;

import java.io.*;

/**
 * Visitor模式的Visitable
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */
public interface Linkable {

  public void accpet(QueueWorker worker) throws Exception;

  public OutputStream getOutputStream();

  public void setInputStream(InputStream in);

}