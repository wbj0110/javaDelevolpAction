package com.jdon.bussinessproxy.remote.http;

import com.jdon.util.Debug;
import com.jdon.bussinessproxy.EJBDefinition;
import com.jdon.bussinessproxy.remote.http.util.Base64;

import java.io.*;
import java.net.*;
import java.util.*;

/*
 * Http连接处理工具帮助类
* <p>Copyright: Jdon.com Copyright (c) 2003</p>
* <p>Company: 上海解道计算机技术有限公司</p>
 */
public class HttpConnectionHelper {
  /**
   * 连接Http Server， 准备传送serialized-object
   */
  public HttpURLConnection connectService(HttpServerParam httpServerParam,
                                          String UserPassword) throws
      Exception {
    HttpURLConnection httpURLConnection = null;
    URL url = null;
    try {
      //获得远程服务器的URL实例
      url = new URL("http", httpServerParam.getHost(),
                    httpServerParam.getPort(), httpServerParam.getServletPath());

      Debug.logVerbose("Service url=" + url, module);
      //打开远程连接
      httpURLConnection = (HttpURLConnection) url.openConnection();
      //提交方式是POST
      httpURLConnection.setRequestMethod("POST");
      httpURLConnection.setDoOutput(true);
      httpURLConnection.setDoInput(true);
      httpURLConnection.setUseCaches(false);
//内容类型是可序列化的对象
      httpURLConnection.setRequestProperty("Content-Type",
          "application/x-java-serialized-object");
      //将登陆信息保存在Http头部
      String encoded = "Basic " + Base64.encode(UserPassword.getBytes("UTF-8"));
      httpURLConnection.setRequestProperty("Authorization", encoded);

    } catch (Exception ex) {
      Debug.logError(" connectServer " + url + " error: " + ex, module);
      throw new Exception(ex);
    }
    return httpURLConnection;
  }

  /**
   * 用于第一次登陆远程服务器
   */
  public HttpURLConnection connectLogin(HttpServerParam httpServerParam,
                                        String UserPassword) throws
      Exception {

    HttpURLConnection httpURLConnection = null;
    URL url = null;
    try {
      url = new URL("http", httpServerParam.getHost(),
                    httpServerParam.getPort(), httpServerParam.getLoginPath());
      Debug.logVerbose("login url=" + url, module);
      httpURLConnection = (HttpURLConnection) url.openConnection();
      httpURLConnection.setRequestMethod("POST");
      httpURLConnection.setDoOutput(true);
      httpURLConnection.setDoInput(true);
      httpURLConnection.setUseCaches(false);
      //内容类型是正常的表单
      httpURLConnection.setRequestProperty("Content-Type",
                                          "application/x-www-form-urlencoded");
      //将登陆信息保存在Http头部
      String encoded = "Basic " + Base64.encode(UserPassword.getBytes("UTF-8"));
      httpURLConnection.setRequestProperty("Authorization", encoded);
    } catch (Exception ex) {
      Debug.logError(" connectServer " + url + " error: " + ex, module);
      throw new Exception(ex);
    }
    return httpURLConnection;
  }

  /**
   * 将可序列化Object发往Http Server
   */
  public void sendObjectRequest(HttpURLConnection httpURLConnection,
                                Object request) throws Exception {
    try {
      //send the request query object to the server
      ObjectOutputStream oos = new ObjectOutputStream(
          httpURLConnection.getOutputStream());
      oos.writeObject(request);
      oos.close();
    } catch (Exception ex) {
      Debug.logError(ex, module);
      throw new Exception(ex);
    }
  }

  /**
   * 将Post的参数字符串发往Http服务器
   * 参数字符串如 "param1=xxx&param2=xxxxx …"
   */
  public void sendDataRequest(HttpURLConnection httpURLConnection,
                              Hashtable param) throws Exception {
    try {
      PrintWriter out = new PrintWriter(new OutputStreamWriter(
          httpURLConnection.getOutputStream()));
      String paramString = "";
      for (Enumeration e = param.keys(); e.hasMoreElements(); ) {
        String key = (String) e.nextElement();
        String value = (String) param.get(key);
        // no harm for an extra & at the end of the parameter list
        paramString += key + "=" + URLEncoder.encode(value, "UTF-8") + "&";
      }
      paramString = paramString.substring(0, paramString.length() - 1);
      out.println(paramString);
      out.close();
    } catch (Exception ex) {
      Debug.logError(ex, module);
      throw new Exception(ex);
    }
  }

  /**
   * 从Http Server的响应中获取Object
   */
  public Object getObjectResponse(HttpURLConnection httpURLConnection) throws
      Exception {
    Object object = null;
    try {
      ObjectInputStream ois = new ObjectInputStream(
          httpURLConnection.getInputStream());
      object = ois.readObject();
      ois.close();
    } catch (Exception ex) {
      Debug.logError(ex, module);
      throw new Exception(ex);
    }
    return object;
  }

  public String getStringResponse(HttpURLConnection httpURLConnection) throws
      Exception {
    StringBuffer sb = new StringBuffer();
    try {
      //测试时打印
      BufferedReader in = new BufferedReader(new InputStreamReader(
          httpURLConnection.getInputStream()));
      String buffer = "";
      while ( (buffer = in.readLine()) != null) {
        sb.append(buffer);
        Debug.logVerbose(buffer, module);
      }
      in.close();
    } catch (Exception ex) {
      Debug.logError(ex, module);
      throw new Exception(ex);
    }
    return sb.toString();
  }
}
