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
* 本类是服务器端处理远程客户端发送的调用EJB请求。
* 本类功能也可以使用Struts的Action实现。
 * 权限验证是使用J2EE的基于Http的Basic Auth.
 * 使用本类可以作为一个单独的EJB网关服务器。
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p>Company: 上海解道计算机技术有限公司</p>
 * @author banq
 * @version 1.0
 */
public class InvokerServlet extends HttpServlet {
  public final static String module = InvokerServlet.class.getName();
  // doGet主要是用于浏览器测试时使用
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
      //如果是login 显示欢迎信息或其它更新通知等信息
      doGet(request, response);
      return;
    }

    //从HttpServletRequest中获得传送的对象
    HttpRequest httpRequest = getHttpServiceRequest(request);
    HttpResponse httpResponse = null;

    try {
      EJBDefinition eJBDefinition = httpRequest.getEJBDefinition();
      String p_methodName = httpRequest.getMethodName();
      Class[] paramTypes = httpRequest.getParamTypes();
      Object[] p_args = httpRequest.getArgs();

      HttpSession session = request.getSession(true);
      //通过HttpSessionProxy调用EJB
      Object object = HttpSessionProxy.handleInvoker (eJBDefinition, session,
                                                  p_methodName,
                                                  paramTypes, p_args);
     //将结果序列化进入Http响应信号
      httpResponse = new HttpResponse(object);
      writeHttpServiceResponse(response, httpResponse);
    } catch (Exception e) {
Debug.logError(e, module);
    }
  }

  /**
   * 序列化处理结果
   */
  private void writeHttpServiceResponse(HttpServletResponse response,
                        HttpResponse httpResponse) throws Exception {
    OutputStream outputStream = response.getOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
    oos.writeObject(httpResponse);
    oos.close();
  }

  /**
   * 从请求信息中反序列化
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
