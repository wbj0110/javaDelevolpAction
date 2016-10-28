package com.jdon.bussinessproxy.remote;

import com.jdon.util.Debug;

import javax.servlet.http.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.jdon.bussinessproxy.EJBDefinition;
import com.jdon.bussinessproxy.remote.http.HttpClient;
import com.jdon.bussinessproxy.remote.auth.Authenticator;

/*
 * 动态代理InvocationHandler
* <p>Copyright: Jdon.com Copyright (c) 2003</p>
* <p>Company: 上海解道计算机技术有限公司</p>
 */
public class RemoteInvocationHandler implements InvocationHandler {
  //获得Http访问客户端HttpClient单态实例
  private final static HttpClient httpClient =  HttpClient.getInstance();
  private EJBDefinition eJBDefinition = null;
  public RemoteInvocationHandler(EJBDefinition eJBDefinition) {
    this.eJBDefinition = eJBDefinition;
  }
  //每调用一次远程EJB方法，将激活本方法
  public Object invoke(Object p_proxy, Method method, Object[] args) throws
      Throwable {
    Debug.logVerbose("method:" + method.getName(), module);

    if (method.getName().equals(Authenticator.AUTH_METHOD_NAME))
      //如果是login
      return httpClient.invokeAuth(args);
    else
       return httpClient.invoke(eJBDefinition, method, args);
  }
}
