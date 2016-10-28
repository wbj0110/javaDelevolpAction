package com.jdon.jserver.http;

import com.jdon.jserver.connector.data.*;
import java.io.*;
import java.util.*;

import com.jdon.util.ObjectStreamUtil;

/**
 * WrapFactory�ľ���ʵ��
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
 * @author banq
 * @version 1.0
 */

public class HttpWrapFactory extends com.jdon.jserver.connector.WrapFactory {

  private String httpPOSTHeader = null;

  public HttpWrapFactory() {
    httpPOSTHeader = HttpHeadHelper.getPOSTHeader("", 0);
  }

  public  byte[] getRequest(byte[] bytes){
     return HttpHeadHelper.assembleRequest(bytes);
  }

  public byte[] getResponse(byte[] bytes){
     return HttpHeadHelper.assembleResponse(bytes);
  }

  public byte[] getContentFromRequest(byte[] bytes) throws Exception{
     return HttpHeadHelper.getContent(bytes);
  }

  public byte[] getContentFromResponse(byte[] bytes) throws Exception{
    return HttpHeadHelper.getContent(bytes);
  }

}