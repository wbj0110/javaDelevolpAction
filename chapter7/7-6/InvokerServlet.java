package com.jdon.bussinessproxy.remote.http;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;

import com.jdon.bussinessproxy.ServiceServerFactory;
import com.jdon.bussinessproxy.EJBDefinition;
import com.jdon.bussinessproxy.web.session.HttpSessionProxy;

import com.jdon.util.Debug;

/**
* �����Ƿ������˴���Զ�̿ͻ��˷��͵ĵ���EJB����
* ���๦��Ҳ����ʹ��Struts��Actionʵ�֡�
 * Ȩ����֤��ʹ��J2EE�Ļ���Http��Basic Auth.
 * ʹ�ñ��������Ϊһ��������EJB���ط�������
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: �Ϻ����������������޹�˾</p>
 * @author banq
 * @version 1.0
 */
public class InvokerServlet extends HttpServlet {
  public final static String module = InvokerServlet.class.getName();
  // doGet��Ҫ���������������ʱʹ��
  protected void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException,
      IOException {
    response.setContentType("text/html");
    PrintWriter out = new PrintWriter(response.getOutputStream());
    out.println("<html>");
    out.println("<head><title>InvokerServlet</title></head>");
    out.println("<body> ");
    out.println(request.getRemoteUser());
    out.println(" Welcome to login in !!</body></html>");
    out.close();
  }

  protected void doPost(HttpServletRequest request,
                        HttpServletResponse response) throws ServletException,
      IOException {
    if (request.getParameter("login") != null) {
      //�����login ��ʾ��ӭ��Ϣ����������֪ͨ����Ϣ
      doGet(request, response);
      return;
    }

    //��HttpServletRequest�л�ô��͵Ķ���
    HttpRequest httpRequest = getHttpServiceRequest(request);
    HttpResponse httpResponse = null;

    try {
      EJBDefinition eJBDefinition = httpRequest.getEJBDefinition();
      String p_methodName = httpRequest.getMethodName();
      Class[] paramTypes = httpRequest.getParamTypes();
      Object[] p_args = httpRequest.getArgs();

      HttpSession session = request.getSession(true);
      //ͨ��HttpSessionProxy����EJB
      Object object = HttpSessionProxy.handleInvoker (eJBDefinition, session,
                                                  p_methodName,
                                                  paramTypes, p_args);
     //��������л�����Http��Ӧ�ź�
      httpResponse = new HttpResponse(object);
      writeHttpServiceResponse(response, httpResponse);
    } catch (Exception e) {
Debug.logError(e, module);
    }
  }

  /**
   * ���л�������
   */
  private void writeHttpServiceResponse(HttpServletResponse response,
                        HttpResponse httpResponse) throws Exception {
    OutputStream outputStream = response.getOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
    oos.writeObject(httpResponse);
    oos.close();
  }

  /**
   * ��������Ϣ�з����л�
   */
  private HttpRequest getHttpServiceRequest(HttpServletRequest request) throws
      Exception {
    ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
    HttpRequest httpServiceRequest = null;
    try {
      httpServiceRequest = (HttpRequest) ois.readObject();
    } catch (ClassNotFoundException e) {
      Debug.logError(e, module);
    }
    ois.close();
    return httpServiceRequest;
  }
}
