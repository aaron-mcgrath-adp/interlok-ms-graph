package com.adaptris.msgraph.mail;

import java.util.ArrayList;
import java.util.List;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.microsoft.graph.models.extensions.EmailAddress;
import com.microsoft.graph.models.extensions.Message;
import com.microsoft.graph.models.extensions.Recipient;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("configured-mail-definition-provider")
public class ConfiguredMailDefinitionProvider implements MailDefinitionProvider {

  private String subject;
  
  private List<String> recipients;
  
  private String from;
  
  public ConfiguredMailDefinitionProvider() {
    this.setRecipients(new ArrayList<>());
  }
  
  @Override
  public Message composeMessage(AdaptrisMessage message) throws CoreException {
    Message mailMessage = new Message();
    mailMessage.subject = this.getSubject();
    mailMessage.toRecipients = this.createRecipients(this.getRecipients());
    
    EmailAddress address = new EmailAddress();
    address.address = this.getFrom();
    Recipient from = new Recipient();
    from.emailAddress = address;
    
    mailMessage.from = from; 
    
    return mailMessage;
  }

  private List<Recipient> createRecipients(List<String> recipientsList) {
    List<Recipient> returnedList = new ArrayList<>();
    
    for(String mailAddress : recipientsList) {
      EmailAddress address = new EmailAddress();
      address.address = mailAddress;
      Recipient recipient = new Recipient();
      recipient.emailAddress = address;
      
      returnedList.add(recipient);
    }
    return returnedList;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public List<String> getRecipients() {
    return recipients;
  }

  public void setRecipients(List<String> recipients) {
    this.recipients = recipients;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

}
