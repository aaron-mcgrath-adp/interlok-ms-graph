package com.adaptris.msgraph;

import java.util.ArrayList;
import java.util.List;

import com.adaptris.core.AllowsRetriesConnection;
import com.adaptris.core.CoreException;

public class MSGraphConnection extends AllowsRetriesConnection {
  
  private List<String> scopes;
  
  private String clientId;
  
  public MSGraphConnection() {
    this.setScopes(new ArrayList<>());
  }

  @Override
  protected void prepareConnection() throws CoreException {
  }

  @Override
  protected void initConnection() throws CoreException {
    // TODO Auto-generated method stub
    
  }

  @Override
  protected void startConnection() throws CoreException {
    // TODO Auto-generated method stub
    
  }

  @Override
  protected void stopConnection() {
    // TODO Auto-generated method stub
    
  }

  @Override
  protected void closeConnection() {
    // TODO Auto-generated method stub
    
  }

  public List<String> getScopes() {
    return scopes;
  }

  public void setScopes(List<String> scopes) {
    this.scopes = scopes;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

}
