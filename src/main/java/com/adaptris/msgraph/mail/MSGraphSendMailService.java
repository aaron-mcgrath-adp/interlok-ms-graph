package com.adaptris.msgraph.mail;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.msgraph.MSGraphBaseService;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ms-graph-send-mail-service")
@AdapterComponent
@ComponentProfile(summary = "Calls the MS Graph mail api to send a email message with the given details.", tag = "ms-graph,service")
public class MSGraphSendMailService extends MSGraphBaseService {
  
  private MailDefinitionProvider mailDefinitionProvider;
  
  public MSGraphSendMailService() {
    super();
  }

  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    this.configureAuthentication(msg);
    
    try {
      this.getApiUserTarget()
        .createUserTarget(this.getGraphClient(), msg)
        .sendMail(this.getMailDefinitionProvider().composeMessage(msg), true)
        .buildRequest()
        .post();
      
    } catch (CoreException t) {
      throw new ServiceException(t);
    }
  }

  public MailDefinitionProvider getMailDefinitionProvider() {
    return mailDefinitionProvider;
  }

  public void setMailDefinitionProvider(MailDefinitionProvider mailDefinitionProvider) {
    this.mailDefinitionProvider = mailDefinitionProvider;
  }

}
