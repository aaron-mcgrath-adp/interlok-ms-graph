package com.adaptris.msgraph;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.IUserRequestBuilder;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("configured-api-user-target")
@AdapterComponent
@ComponentProfile(summary = "Allows you to configure the Azure AD user for which the request will act.", tag = "ms-graph")
public class ConfiguredApiUserTarget implements ApiUserTarget {

  public String userId;
  
  @Override
  public IUserRequestBuilder createUserTarget(IGraphServiceClient graphClient, AdaptrisMessage message) throws CoreException {
    return graphClient.users(message.resolve(this.getUserId()));
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

}
