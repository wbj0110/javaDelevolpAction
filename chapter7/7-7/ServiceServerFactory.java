package com.jdon.bussinessproxy;

import java.util.*;

import com.jdon.util.Debug;
import javax.servlet.http.*;

public abstract class ServiceServerFactory{
  private static Object initLock = new Object();
  private static String className =
      "com.jdon.bussinessproxy.web.ServiceFactoryImp";
  private static ServiceServerFactory factory = null;

  public static ServiceServerFactory getInstance() {
    if (factory == null) {
      synchronized (initLock) {
        if (factory == null) {
          try {
            //Load the class and create an instance.
            Class c = Class.forName(className);
            factory = (ServiceServerFactory) c.newInstance();
          } catch (Exception e) {
            Debug.logError(" get factory instance error:" + e, module);
            return null;
          }
        }
      }
    }
    return factory;
  }
  //需要实现的抽象方法
  public abstract Object getService(EJBDefinition eJBDefinition,
                                    HttpServletRequest request);
}
