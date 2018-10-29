package com.adaptris.msgraph.auth;

public class MSGraphAccessToken {

  private String accessToken;
  
  private int expiresInSeconds;
  
  private long validFromMillisEpoc;
  
  public MSGraphAccessToken() {
    
  }
  
  public boolean isActive() {
    if((this.getAccessToken() != null) && (this.getValidFromMillisEpoc() > 0) && (this.getExpiresInSeconds() > 0)) {
      long now = System.currentTimeMillis();
      
      if(now >= (this.getValidFromMillisEpoc() + (this.getExpiresInSeconds() * 1000)))
        return false;
      else
        return true;
      
    } else
      return false;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public int getExpiresInSeconds() {
    return expiresInSeconds;
  }

  public void setExpiresInSeconds(int expiresInSeconds) {
    this.expiresInSeconds = expiresInSeconds;
  }

  public long getValidFromMillisEpoc() {
    return validFromMillisEpoc;
  }

  public void setValidFromMillisEpoc(long validFromMillisEpoc) {
    this.validFromMillisEpoc = validFromMillisEpoc;
  }

  
}
