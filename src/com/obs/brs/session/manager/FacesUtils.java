
package com.obs.brs.session.manager;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//BV Use kisra NumberUtil
public class FacesUtils {

  public void setRequestAttribute(String attribute, long value) {
    getRequest().setAttribute(attribute, new Long(value));
  }

  public void setRequestAttribute(String name, Object value) {
    getRequest().setAttribute(name, value);
  }
 
  public String getRequestParameterMap(String parameter)
  {
	  return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(parameter);
  }
  
  public HashMap<String, Object> getRequestMap(String key,Object map)
  {
	  return (HashMap<String, Object>) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(key, map);
  }
  public HashMap getRequestMap(String key)
  {
	  return (HashMap) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(key);
  }
  

  public boolean hasRequestAttribute(String attributeName) {
    return getRequest().getAttribute(attributeName) != null;
  }
  
  public HttpServletRequest getRequest() {
    FacesContext        context = FacesContext.getCurrentInstance();  
    HttpServletRequest  request = (HttpServletRequest)context.getExternalContext().getRequest();
    return request;
  }

  public HttpServletResponse getResponse() {
    FacesContext context = FacesContext.getCurrentInstance();  
    return (HttpServletResponse)context.getExternalContext().getResponse();
  }
  

  public String getRequestParameter(String parameterName) {
    Map requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    return (String) requestParameterMap.get(parameterName);
  }

  public Object getRequestAttribute(String attributeName) {
    return getRequest().getAttribute(attributeName);
  }

  public void silentlyEndFacesResponse() {
    FacesContext context = FacesContext.getCurrentInstance();
    if (context != null)
      context.responseComplete();  
  }

  public long getLongComponentAttributeValue(ActionEvent ae, String attributeName) {
    Object attribute = getComponentAttribute(ae, attributeName);
    return ((Long)attribute).longValue();
  }

  public String getStringComponentAttributeValue(ActionEvent ae, String attributeName) {
    Object attribute = getComponentAttribute(ae, attributeName);
    return (String)attribute;
  }


  public static Object getComponentObject( String componentId ) {
	  FacesContext context = FacesContext.getCurrentInstance();
		return context.getViewRoot().findComponent( componentId );
  }
  
  private Object getComponentAttribute(FacesEvent facesEvent, String attributeName) {
    Map attributes = facesEvent.getComponent().getAttributes();
    return attributes.get(attributeName);
  }
  
  public FacesContext getContext(){
		return FacesContext.getCurrentInstance();
	}


}
