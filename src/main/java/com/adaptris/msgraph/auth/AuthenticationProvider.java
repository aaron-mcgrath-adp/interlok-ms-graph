package com.adaptris.msgraph.auth;

import com.adaptris.core.AdaptrisMessage;
import com.microsoft.graph.authentication.IAuthenticationProvider;

public interface AuthenticationProvider extends IAuthenticationProvider {
  
  public void setAdaptrisMessage(AdaptrisMessage message);
  
  public void setTokenKey(String key);

}
