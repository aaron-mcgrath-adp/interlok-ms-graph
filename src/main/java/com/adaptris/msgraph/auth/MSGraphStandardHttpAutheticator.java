package com.adaptris.msgraph.auth;

import java.net.URLEncoder;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.ConfiguredProduceDestination;
import com.adaptris.core.CoreException;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.http.ConfiguredContentTypeProvider;
import com.adaptris.core.http.client.ConfiguredRequestMethodProvider;
import com.adaptris.core.http.client.RequestMethodProvider.RequestMethod;
import com.adaptris.core.http.client.net.StandardHttpProducer;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.security.password.Password;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MSGraphStandardHttpAutheticator implements MSGraphAuthenticator {
  
  private static final Long DEFAULT_REQUEST_TIMEOUT_MILLIS = 5000L;
  
  private static final String DEFAULT_REQUEST_CONTENT_TYPE = "application/x-www-form-urlencoded";
  
  private static final String DEFAULT_AUTHENTICATION_ENDPOINT = "https://login.microsoftonline.com/common/oauth2/v2.0/token";
  
  private static final String DEFAULT_SCOPE = "https://graph.microsoft.com/.default";
  
  private static final String DEFAULT_GRANT_TYPE = "client_credentials";

  private String authenticationEndpoint;
  
  private String clientId;
  
  private String scope;
  
  private String secret;
  
  private String grantType;
  
  private transient StandardHttpProducer httpProducer;
  
  public MSGraphStandardHttpAutheticator() {
    
  }
  
  @Override
  public MSGraphAccessToken authenticate() throws CoreException {
    return this.decodeJsonResponse(this.fireHttpAutheticationRequest());
  }

  private MSGraphAccessToken decodeJsonResponse(String jsonResponse) throws CoreException {
    long validFrom = System.currentTimeMillis();
    
    JsonParser parser = new JsonParser();
    JsonArray jsonArray = (JsonArray) parser.parse(jsonResponse);
    
    if(jsonArray.size() == 1) {
      JsonObject expiry = (JsonObject) jsonArray.get(0);

      int expiryInt = (int) expiry.get("expires_in").getAsInt();
      String accessToken = (String) expiry.get("access_token").getAsString();

      MSGraphAccessToken returnedToken = new MSGraphAccessToken();
      returnedToken.setAccessToken(accessToken);
      returnedToken.setExpiresInSeconds(expiryInt);
      returnedToken.setValidFromMillisEpoc(validFrom);
      
      return returnedToken;
    } else
      throw new CoreException("Expected single json response element, but got " + jsonArray.size());
  }

  private String fireHttpAutheticationRequest() throws CoreException {
    StandardHttpProducer httpProducer = new StandardHttpProducer(new ConfiguredProduceDestination(this.authenticationEndpoint()));
    httpProducer.setContentTypeProvider(new ConfiguredContentTypeProvider(DEFAULT_REQUEST_CONTENT_TYPE));
    httpProducer.setAlwaysSendPayload(true);
    httpProducer.setMethodProvider(new ConfiguredRequestMethodProvider(RequestMethod.POST));
    
    StringBuilder httpRequestPayload = new StringBuilder();
    try {
      httpRequestPayload.append("client_id=");
      httpRequestPayload.append(this.getClientId());
      httpRequestPayload.append("&scope=");
      httpRequestPayload.append(URLEncoder.encode(this.scope(), "UTF-8"));
      httpRequestPayload.append("&client_secret=");
      httpRequestPayload.append(Password.decode(this.getSecret()));
      httpRequestPayload.append("&grant_type=");
      httpRequestPayload.append(this.grantType());
    } catch (Exception ex) {
      throw new CoreException(ex);
    }
    
    AdaptrisMessage tempMessage = DefaultMessageFactory.getDefaultInstance().newMessage(httpRequestPayload.toString());
    
    LifecycleHelper.initAndStart(httpProducer);
    httpProducer.request(tempMessage, DEFAULT_REQUEST_TIMEOUT_MILLIS);
    LifecycleHelper.stopAndClose(httpProducer);
    
    return tempMessage.getContent();
  }

  public String authenticationEndpoint() {
    return this.getAuthenticationEndpoint() != null ? this.getAuthenticationEndpoint() : DEFAULT_AUTHENTICATION_ENDPOINT;
  }
  
  public String getAuthenticationEndpoint() {
    return authenticationEndpoint;
  }

  public void setAuthenticationEndpoint(String authenticationEndpoint) {
    this.authenticationEndpoint = authenticationEndpoint;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String scope() {
    return this.getScope() != null ? this.getScope() : DEFAULT_SCOPE;
  }
  
  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String grantType() {
    return this.getGrantType() != null ? this.getGrantType() : DEFAULT_GRANT_TYPE;
  }
  
  public String getGrantType() {
    return grantType;
  }

  public void setGrantType(String grantType) {
    this.grantType = grantType;
  }

  public StandardHttpProducer getHttpProducer() {
    return httpProducer;
  }

  public void setHttpProducer(StandardHttpProducer httpProducer) {
    this.httpProducer = httpProducer;
  }

}
