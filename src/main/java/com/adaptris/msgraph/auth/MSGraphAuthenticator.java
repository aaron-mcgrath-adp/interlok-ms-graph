package com.adaptris.msgraph.auth;

import com.adaptris.core.CoreException;

public interface MSGraphAuthenticator {

  public MSGraphAccessToken authenticate() throws CoreException;
  
}
