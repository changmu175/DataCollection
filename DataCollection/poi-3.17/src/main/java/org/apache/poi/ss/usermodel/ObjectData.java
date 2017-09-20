package org.apache.poi.ss.usermodel;

import java.io.IOException;
import org.apache.poi.poifs.filesystem.DirectoryEntry;

public abstract interface ObjectData
  extends SimpleShape
{
  public abstract byte[] getObjectData()
    throws IOException;
  
  public abstract boolean hasDirectoryEntry();
  
  public abstract DirectoryEntry getDirectory()
    throws IOException;
  
  public abstract String getOLE2ClassName();
  
  public abstract String getFileName();
  
  public abstract PictureData getPictureData();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.ObjectData
 * JD-Core Version:    0.7.0.1
 */