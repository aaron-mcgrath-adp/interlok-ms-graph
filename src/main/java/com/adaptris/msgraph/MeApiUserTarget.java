package com.adaptris.msgraph;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.IUserRequestBuilder;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("me-api-user-target")
public class MeApiUserTarget implements ApiUserTarget {

  @Override
  public IUserRequestBuilder createUserTarget(IGraphServiceClient graphClient, AdaptrisMessage message) throws CoreException {
    return graphClient.me();
  }

}
