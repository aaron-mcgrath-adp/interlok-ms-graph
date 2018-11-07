package com.adaptris.msgraph;

import org.hibernate.validator.constraints.NotBlank;

import com.adaptris.annotation.AffectsMetadata;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceImp;
import com.adaptris.msgraph.auth.AuthenticationProvider;
import com.adaptris.msgraph.auth.MSGraphAuthenticationProvider;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;

public abstract class MSGraphBaseService extends ServiceImp {
  
  private transient IGraphServiceClient graphClient;
  
  private AuthenticationProvider authenticationProvider;
    
  private ApiUserTarget apiUserTarget;
  
  @NotBlank
  @AffectsMetadata
  @InputFieldDefault(value = "Authorization")
  private String tokenKey;
  
  public MSGraphBaseService() {
    this.setAuthenticationProvider(new MSGraphAuthenticationProvider());
  }
  
  protected void configureAuthentication(AdaptrisMessage msg) {
    this.getAuthenticationProvider().setAdaptrisMessage(msg);
    this.getAuthenticationProvider().setTokenKey(msg.resolve(this.getTokenKey()));
  }

  @Override
  public void prepare() throws CoreException {
  }

  @Override
  protected void initService() throws CoreException {
    this.setGraphClient(GraphServiceClient
        .builder()
        .authenticationProvider(authenticationProvider)
        .buildClient());
  }

  @Override
  protected void closeService() {
  }

  public IGraphServiceClient getGraphClient() {
    return graphClient;
  }

  public void setGraphClient(IGraphServiceClient graphClient) {
    this.graphClient = graphClient;
  }

  public AuthenticationProvider getAuthenticationProvider() {
    return authenticationProvider;
  }

  public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
    this.authenticationProvider = authenticationProvider;
  }

  public ApiUserTarget getApiUserTarget() {
    return apiUserTarget;
  }

  public void setApiUserTarget(ApiUserTarget apiUserTarget) {
    this.apiUserTarget = apiUserTarget;
  }

  public String getTokenKey() {
    return tokenKey;
  }

  public void setTokenKey(String tokenKey) {
    this.tokenKey = tokenKey;
  }

}
