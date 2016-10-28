package com.jdon.bussinessproxy.remote;

import com.jdon.util.Debug;

import javax.servlet.http.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.jdon.bussinessproxy.EJBDefinition;
import com.jdon.bussinessproxy.remote.http.HttpClient;
import com.jdon.bussinessproxy.remote.auth.Authenticator;

/*
 * ��̬����InvocationHandler
* <p>Copyright: Jdon.com Copyright (c) 2003</p>
* <p>Company: �Ϻ����������������޹�˾</p>
 */
public class RemoteInvocationHandler implements InvocationHandler {
  //���Http���ʿͻ���HttpClient��̬ʵ��
  private final static HttpClient httpClient =  HttpClient.getInstance();
  private EJBDefinition eJBDefinition = null;
  public RemoteInvocationHandler(EJBDefinition eJBDefinition) {
    this.eJBDefinition = eJBDefinition;
  }
  //ÿ����һ��Զ��EJB���������������
  public Object invoke(Object p_proxy, Method method, Object[] args) throws
      Throwable {
    Debug.logVerbose("method:" + method.getName(), module);

    if (method.getName().equals(Authenticator.AUTH_METHOD_NAME))
      //�����login
      return httpClient.invokeAuth(args);
    else
       return httpClient.invoke(eJBDefinition, method, args);
  }
}
