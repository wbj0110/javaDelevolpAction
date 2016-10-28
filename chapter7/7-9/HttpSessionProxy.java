package com.jdon.bussinessproxy.web.session;

import javax.servlet.http.*;
import com.jdon.bussinessproxy.EJBDefinition;
import com.jdon.util.Debug;

import java.lang.reflect.Method;
import com.jdon.bussinessproxy.ejbproxy.EJBProxyHandler;

/**
 * 使用HttpSession作为EJB session bean的缓存
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */
public class HttpSessionProxy {
  private final static String module = HttpSessionProxy.class.getName();
  /**
   * EJB方法调用
   */
  public static Object handleInvoker(EJBDefinition eJBDefinition,
                                     HttpSession session, String p_methodName,
                                     Class[] paramTypes, Object[] args) {
    Debug.logVerbose("--> enter handleInvoker ...", module);
    Object result = null;
    try {
      Object objRef = getEJBServiceRef(eJBDefinition, session);
      Method invokedMethod = objRef.getClass().getMethod(p_methodName,
          paramTypes);
      result = invokedMethod.invoke(objRef, EJBProxyHandler.narrowArgs(args));
      Debug.logVerbose("Invoked Successfully : " + p_methodName, module);
    } catch (Exception ex) {
      Debug.logError("handleInvoker error: " + ex, module);
    }
    return result;

  }

  /**
   * 获取EJBLocalObject或EJBObject
   * 首先从HttpSession中获取，如果没有，再直接生成，然后
   * 放入HttpSession中备下次使用
   */
  private static Object getEJBServiceRef(EJBDefinition eJBDefinition,
                                         HttpSession session) {
    if (session == null) Debug.logVerbose(" --> session is null", module);
    Object ccEjb = null;
    try {
      ccEjb = HttpSessionProxy.getFromCache(eJBDefinition, session);
      if (ccEjb == null) {
        Debug.logVerbose(" --> the service not existed in HttpSession", module);
        ccEjb = EJBProxyHandler.getHomeRef(eJBDefinition);
        HttpSessionProxy.putCache(eJBDefinition, ccEjb, session);
      } else {
        Debug.logVerbose(" --> got the service from the HttpSession" +
                         ccEjb.getClass().getName(), module);
      }
    } catch (Exception ex) {
      Debug.logError(" getEJBServiceRef error:" + ex, module);
    }
    return ccEjb;
  }
  /**
   * 从HttpSession中获取EJB Object
   * 使用ComponentManager保存各种EJB Object
   */
  public static Object getFromCache(EJBDefinition eJBDefinition,
                                    HttpSession session) throws Exception {
    Object ccEjb = null;
    try {
      ComponentManager cm = (ComponentManager) session.getAttribute(
          ComponentManager.getComponentName());
      if (cm != null) {
        Debug.logVerbose(" --> get from Http Session ", module);
        ccEjb = cm.getObject(eJBDefinition.toString());
      }
    } catch (Exception ex) {
      Debug.logError(ex, module);
      throw new Exception(ex);
    }
    return ccEjb;
  }
  /**
   * 将EJB Object 保存到HttpSession中
   * 使用ComponentManager保存各种EJB Object
   */
  public static void putCache(EJBDefinition eJBDefinition, Object ccEjb,
                              HttpSession session) throws
      Exception {
    try {
      Debug.logVerbose(" --> putCache", module);
      ComponentManager cm = new ComponentManager();
      cm.putObject(eJBDefinition.toString(), ccEjb);
      session.setAttribute(ComponentManager.getComponentName(), cm);
    } catch (Exception ex) {
      Debug.logError(ex, module);
      throw new Exception(ex);
    }
  }
}
