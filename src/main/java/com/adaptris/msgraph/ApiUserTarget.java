package com.adaptris.msgraph;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.IUserRequestBuilder;

public interface ApiUserTarget {
  
  public IUserRequestBuilder createUserTarget(IGraphServiceClient graphClient, AdaptrisMessage message) throws CoreException;

}
