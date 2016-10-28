package com.jdon.bussinessproxy;

import java.io.Serializable;


public class EJBDefinition implements Serializable {
  private String _jndiName;
  private String _local;
  public EJBDefinition(String p_jndiName, String p_localClassName) {
    _jndiName = p_jndiName;
    _local = p_localClassName;
  }
  public Class getEJBInterfaceClass() {
        return getLocalClass();
  }
  public Class getLocalClass() {
    try {
      return Class.forName(_local);
    } catch (ClassNotFoundException ex) {
      throw new RuntimeException("Unable to load the class : " + _local);
    }
  }
  public String getLocalName() {    return _local;  }
  public String getJndiName() {    return _jndiName;  }

}
