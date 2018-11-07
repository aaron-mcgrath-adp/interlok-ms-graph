package com.adaptris.msgraph.onedrive;

import org.hibernate.validator.constraints.NotBlank;

import com.adaptris.msgraph.MSGraphBaseService;
import com.microsoft.graph.requests.extensions.IDriveItemCollectionRequestBuilder;
import com.microsoft.graph.requests.extensions.IDriveItemRequestBuilder;

public abstract class MSGraphBaseOneDriveService extends MSGraphBaseService {

  @NotBlank
  private String driveId;
  
  @NotBlank
  private String path;
  
  /**
   * Path to the directory of the file to upload.  "my/path/to/the/directory"
   * @param requestBuilder 
   * @param dirPath
   * @param newFileName 
   * @return
   */
  protected IDriveItemRequestBuilder buildPath(IDriveItemCollectionRequestBuilder requestBuilder, String dirPath, String newFileName) {
    String[] dirs = dirPath.split("/");
    for(String dir : dirs) {
      requestBuilder.byId(dir).children();
    }
    
    return requestBuilder.byId(newFileName);
  }
  
  public String getDriveId() {
    return driveId;
  }

  public void setDriveId(String driveId) {
    this.driveId = driveId;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
  
}
