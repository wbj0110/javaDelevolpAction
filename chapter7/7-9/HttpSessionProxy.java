package com.jdon.bussinessproxy.web.session;

import javax.servlet.http.*;
import com.jdon.bussinessproxy.EJBDefinition;
import com.jdon.util.Debug;

import java.lang.reflect.Method;
import com.jdon.bussinessproxy.ejbproxy.EJBProxyHandler;

/**
 * ʹ��HttpSession��ΪEJB session bean�Ļ���
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
 * @author banq
 * @version 1.0
 */
public class HttpSessionProxy {
  private final static String module = HttpSessionProxy.class.getName();
  /**
   * EJB��������
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
   * ��ȡEJBLocalObject��EJBObject
   * ���ȴ�HttpSession�л�ȡ�����û�У���ֱ�����ɣ�Ȼ��
   * ����HttpSession�б��´�ʹ��
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
   * ��HttpSession�л�ȡEJB Object
   * ʹ��ComponentManager�������EJB Object
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
   * ��EJB Object ���浽HttpSession��
   * ʹ��ComponentManager�������EJB Object
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
