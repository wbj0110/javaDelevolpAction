package com.jdon.bussinessproxy.web;

import com.jdon.bussinessproxy.EJBDefinition;
import java.lang.reflect.Proxy;
import java.util.*;

import javax.servlet.http.*;
import com.jdon.util.Debug;

/**
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */

public class ServiceFactoryImp extends ServiceClientFactory {
  //代理实例的缓存
  private static Map _proxyCache = new Hashtable();

  public void setHttpServerParam(HttpServerParam httpServerParam) {
     httpClient.setHttpServerParam(httpServerParam);
  }

  //首先从缓存中获得代理实例，如果没有，通过动态代理生成。
  public Object getService(EJBDefinition eJBDefinition) {
    Debug.logVerbose(" --> enter getService from dynamicProxy", module);
    Object dynamicProxy = _proxyCache.get(eJBDefinition);
    if (dynamicProxy == null) {
      dynamicProxy = getServiceFromProxy(eJBDefinition);
      _proxyCache.put(eJBDefinition, dynamicProxy);
    }
    return dynamicProxy;
  }

  //第一次登陆验证
  public String login(String loginName, String password) throws AuthException{
    String loginResult = null;
    try{
      Debug.logVerbose(" --> enter login", module);
      //因为是J2EE容器安全验证，因此虚构一个EJBDefinition
      EJBDefinition eJBDefinition = new EJBDefinition(
          "NoEJB", "com.jdon.bussinessproxy.remote.auth.Authenticator");
      //虚构一个Authenticator实例
      Authenticator authenticator = (Authenticator) getService(eJBDefinition);
      //激活HttpClient的invokeAuth方法
      loginResult = authenticator.login(loginName, password);
    }catch(Exception e){
      throw new AuthException(e);
    }
    return loginResult;
  }

  //通过动态代理获得代理实例
  public Object getServiceFromProxy(EJBDefinition eJBDefinition) {
    RemoteInvocationHandler handler = null;
    Object dynamicProxy = null;
    try {
      Debug.logVerbose(" ---> create a new ProxyInstance", module);
      handler = new RemoteInvocationHandler(eJBDefinition);
      dynamicProxy = Proxy.newProxyInstance(RemoteInvocationHandler.class.
                                            getClassLoader(),
                                            new Class[] {
                                            eJBDefinition.getEJBInterfaceClass()
                                            }, handler);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return dynamicProxy;
  }
}
