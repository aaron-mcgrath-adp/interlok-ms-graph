package com.adaptris.msgraph.mail;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.msgraph.MSGraphBaseService;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ms-graph-get-messages-service")
@AdapterComponent
@ComponentProfile(summary = "Builds a JSON representation of multiple messages from the given mail folder and replaces the message content.", tag = "ms-graph,service")
public class MSGraphGetMessagesService extends MSGraphBaseService {

  private String mailFolder;
  
  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    this.configureAuthentication(msg);
    
    try {
      msg.setContent(this.getApiUserTarget()
        .createUserTarget(this.getGraphClient(), msg)
        .mailFolders(msg.resolve(this.getMailFolder()))
        .messages()
        .buildRequest()
        .get().getRawObject().toString()
        , msg.getContentEncoding());
      
    } catch (CoreException t) {
      throw new ServiceException(t);
    }
    
  }

  public String getMailFolder() {
    return mailFolder;
  }

  public void setMailFolder(String mailFolder) {
    this.mailFolder = mailFolder;
  }

}
