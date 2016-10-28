package com.jdon.bussinessproxy;

import java.util.*;

import com.jdon.util.Debug;
import javax.servlet.http.*;


/*
 * 动态代理工厂
* <p>Copyright: Jdon.com Copyright (c) 2003</p>
* <p>Company: 上海解道计算机技术有限公司</p>
 */
public abstract class ServiceClientFactory {
  private static Object initLock = new Object();
  private static String className =
      "com.jdon.bussinessproxy.remote.ServiceFactoryImp";
  private static ServiceClientFactory factory = null;
  //获得本类的Singleton实例
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
  //下面有三个需要继承实现的方法
  //设置Http连接参数
  public abstract void setHttpServerParam(HttpServerParam httpServerParam);
  //第一次登陆login
  public abstract String login(String loginName, String password) throws AuthException;
  //获得EJB的实例
  public abstract Object getService(EJBDefinition eJBDefinition);
}
