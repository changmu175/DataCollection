package org.apache.poi.poifs.filesystem;

public abstract interface Entry
{
  public abstract String getName();
  
  public abstract boolean isDirectoryEntry();
  
  public abstract boolean isDocumentEntry();
  
  public abstract DirectoryEntry getParent();
  
  public abstract boolean delete();
  
  public abstract boolean renameTo(String paramString);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.Entry
 * JD-Core Version:    0.7.0.1
 */