package com.adaptris.msgraph.onedrive;

import org.hibernate.validator.constraints.NotBlank;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.ServiceException;
import com.adaptris.util.stream.StreamUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ms-graph-get-onedrive-file-service")
@AdapterComponent
@ComponentProfile(summary = "Allows you to stream the contents of a file from a one drive account into the message payload.", tag = "ms-graph,service")
public class MSGraphGetOneDriveFileService extends MSGraphBaseOneDriveService {

  @NotBlank
  private String fileName;
  
  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    this.configureAuthentication(msg);
    
    try {
      StreamUtil.copyAndClose(
        buildPath(this.getApiUserTarget()
            .createUserTarget(this.getGraphClient(), msg)
            .drives(msg.resolve(this.getDriveId()))
            .items(), msg.resolve(this.getPath()), msg.resolve(this.getFileName()))
        .content()
        .buildRequest()
        .get()
      , msg.getOutputStream());

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
