package com.adaptris.msgraph.auth;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.AffectsMetadata;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.options.HeaderOption;
import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("ms-graph-authentication-provider")
@AdapterComponent
@ComponentProfile(summary = "Authenticator for the MS Graph API.", tag = "authenticator")
public class MSGraphAuthenticationProvider implements AuthenticationProvider {
  
  protected transient Logger log = LoggerFactory.getLogger(this.getClass().getName());
  /**
   * The authorization header name.
   */
  private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
  
  private static final String DEFAULT_TOKEN_KEY = "Authorization";
  
  @NotBlank
  @AffectsMetadata
  @InputFieldDefault(value = "Authorization")
  private String tokenKey;
  
  private transient AdaptrisMessage message;
  
  public MSGraphAuthenticationProvider() {
  }
  
  @Override
  public void authenticateRequest(IHttpRequest request) {
    log.debug("Authenticating request, " + request.getRequestUrl());

    // If the request already has an authorization header, do not intercept it.
    for (final HeaderOption option : request.getHeaders()) {
      if (option.getName().equals(AUTHORIZATION_HEADER_NAME)) {
        log.debug("Found an existing authorization header, skipping.");
        return;
      }
    }

    try {
      final String accessToken = getAccessToken();
      request.addHeader(AUTHORIZATION_HEADER_NAME, accessToken);
    } catch (ClientException | CoreException e) {
      final String message = "Unable to authenticate request.";
      final ClientException exception = new ClientException(message, e);
      log.error(message, exception);
      throw exception;
    }
    
  }

  /**
   * Will return the access token from the AdaptrisMessage.
   * The token will be in the form:  "bearer uiochs80q9wrh3i2ojhf0923ujrtp23m.....".
   * @return
   * @throws CoreException
   */
  private String getAccessToken() throws CoreException {
    if(this.getAdaptrisMessage() == null)
      throw new CoreException("No AdaptrisMessage found to retrieve the acce4ss token key.");
    
    String accessToken = this.getAdaptrisMessage().getMetadataValue(this.tokenKey());
    if(accessToken == null)
      throw new CoreException("No access token found in AdaptrisMessage.");
    
    return accessToken;
  }

  public AdaptrisMessage getAdaptrisMessage() {
    return message;
  }

  public void setAdaptrisMessage(AdaptrisMessage message) {
    this.message = message;
  }
  
  protected String tokenKey() {
    return this.getTokenKey() != null ? this.getTokenKey() : DEFAULT_TOKEN_KEY;
  }

  public String getTokenKey() {
    return tokenKey;
  }

  public void setTokenKey(String tokenKey) {
    this.tokenKey = tokenKey;
  }

}
