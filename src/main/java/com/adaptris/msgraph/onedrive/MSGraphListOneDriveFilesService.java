package com.adaptris.msgraph.onedrive;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.ServiceException;
import com.microsoft.graph.models.extensions.ListItem;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ms-graph-list-onedrive-files-service")
@AdapterComponent
@ComponentProfile(summary = "Builds a JSON representation of all items in a given directory and replaces the message content.", tag = "ms-graph,service")
public class MSGraphListOneDriveFilesService extends MSGraphBaseOneDriveService {

  private String directoryName;
  
  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    this.configureAuthentication(msg);
    
    try { 
      ListItem listItem = buildPath(this.getApiUserTarget()
        .createUserTarget(this.getGraphClient(), msg)
        .drives(msg.resolve(this.getDriveId()))
        .items(), msg.resolve(this.getPath()), msg.resolve(this.getDirectoryName()))
        .listItem()
        .buildRequest()
        .get();
      
      msg.setContent(listItem.getRawObject().toString(), msg.getContentEncoding());
    
    } catch (Throwable t) {
      throw new ServiceException(t);
    }
    
  }


  public String getDirectoryName() {
    return directoryName;
  }


  public void setDirectoryName(String directoryName) {
    this.directoryName = directoryName;
  }

}
