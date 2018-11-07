package com.adaptris.msgraph.onedrive;

import org.hibernate.validator.constraints.NotBlank;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.ServiceException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ms-graph-send-onedrive-file-service")
@AdapterComponent
@ComponentProfile(summary = "Allows you to send the contents of the message as a file to a one drive account.", tag = "ms-graph,service")
public class MSGraphSendOneDriveFileService extends MSGraphBaseOneDriveService {
  
  @NotBlank
  private String fileName;

  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    this.configureAuthentication(msg);
    
    try {      
      buildPath(this.getApiUserTarget()
          .createUserTarget(this.getGraphClient(), msg)
          .drives(msg.resolve(this.getDriveId()))
          .items(), msg.resolve(this.getPath()), msg.resolve(this.getFileName()))
      .content()
      .buildRequest()
      .put(msg.getPayload());

    } catch (Throwable t) {
      throw new ServiceException(t);
    }
    
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

}
