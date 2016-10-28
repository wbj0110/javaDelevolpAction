package com.jdon.bussinessproxy.web;

import com.jdon.bussinessproxy.EJBDefinition;
import java.lang.reflect.Proxy;
import java.util.*;

import javax.servlet.http.*;
import com.jdon.util.Debug;

/**
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
 * @author banq
 * @version 1.0
 */

public class ServiceFactoryImp extends ServiceClientFactory {
  //����ʵ���Ļ���
  private static Map _proxyCache = new Hashtable();

  public void setHttpServerParam(HttpServerParam httpServerParam) {
     httpClient.setHttpServerParam(httpServerParam);
  }

  //���ȴӻ����л�ô���ʵ�������û�У�ͨ����̬�������ɡ�
  public Object getService(EJBDefinition eJBDefinition) {
    Debug.logVerbose(" --> enter getService from dynamicProxy", module);
    Object dynamicProxy = _proxyCache.get(eJBDefinition);
    if (dynamicProxy == null) {
      dynamicProxy = getServiceFromProxy(eJBDefinition);
      _proxyCache.put(eJBDefinition, dynamicProxy);
    }
    return dynamicProxy;
  }

  //��һ�ε�½��֤
  public String login(String loginName, String password) throws AuthException{
    String loginResult = null;
    try{
      Debug.logVerbose(" --> enter login", module);
      //��Ϊ��J2EE������ȫ��֤������鹹һ��EJBDefinition
      EJBDefinition eJBDefinition = new EJBDefinition(
          "NoEJB", "com.jdon.bussinessproxy.remote.auth.Authenticator");
      //�鹹һ��Authenticatorʵ��
      Authenticator authenticator = (Authenticator) getService(eJBDefinition);
      //����HttpClient��invokeAuth����
      loginResult = authenticator.login(loginName, password);
    }catch(Exception e){
      throw new AuthException(e);
    }
    return loginResult;
  }

  //ͨ����̬�����ô���ʵ��
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
