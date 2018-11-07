package com.adaptris.msgraph.mail;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.microsoft.graph.models.extensions.Message;

public interface MailDefinitionProvider {
  
  public Message composeMessage(AdaptrisMessage message) throws CoreException;

}
