package com.adaptris.msgraph.auth;

import javax.validation.constraints.NotNull;

import com.adaptris.core.CoreException;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;


public class MSGraphAuthenticationProvider implements IAuthenticationProvider {
  
  @NotNull
  private MSGraphAuthenticator authenticator;
  
  public MSGraphAuthenticationProvider() {
  }
  
  @Override
  public void authenticateRequest(IHttpRequest request) {
    try {
      this.getAuthenticator().authenticate();
    } catch (CoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

  public MSGraphAuthenticator getAuthenticator() {
    return authenticator;
  }

  public void setAuthenticator(MSGraphAuthenticator authenticator) {
    this.authenticator = authenticator;
  }

}
