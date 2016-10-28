package com.jdon.bussinessproxy;

import java.util.*;

import com.jdon.util.Debug;
import javax.servlet.http.*;


/*
 * ��̬������
* <p>Copyright: Jdon.com Copyright (c) 2003</p>
* <p>Company: �Ϻ����������������޹�˾</p>
 */
public abstract class ServiceClientFactory {
  private static Object initLock = new Object();
  private static String className =
      "com.jdon.bussinessproxy.remote.ServiceFactoryImp";
  private static ServiceClientFactory factory = null;
  //��ñ����Singletonʵ��
  public static ServiceClientFactory getInstance() {
    if (factory == null) {
      synchronized (initLock) {
        if (factory == null) {
          try {
            //Load the class and create an instance.
            Class c = Class.forName(className);
            factory = (ServiceClientFactory) c.newInstance();
          } catch (Exception e) {
            Debug.logError(" get factory instance error:" + e, module);
            return null;
          }
        }
      }
    }
    return factory;
  }
  //������������Ҫ�̳�ʵ�ֵķ���
  //����Http���Ӳ���
  public abstract void setHttpServerParam(HttpServerParam httpServerParam);
  //��һ�ε�½login
  public abstract String login(String loginName, String password) throws AuthException;
  //���EJB��ʵ��
  public abstract Object getService(EJBDefinition eJBDefinition);
}
