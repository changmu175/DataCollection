package org.apache.poi.poifs.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import org.apache.poi.hpsf.ClassID;

public abstract interface DirectoryEntry
  extends Entry, Iterable<Entry>
{
  public abstract Iterator<Entry> getEntries();
  
  public abstract Set<String> getEntryNames();
  
  public abstract boolean isEmpty();
  
  public abstract int getEntryCount();
  
  public abstract boolean hasEntry(String paramString);
  
  public abstract Entry getEntry(String paramString)
    throws FileNotFoundException;
  
  public abstract DocumentEntry createDocument(String paramString, InputStream paramInputStream)
    throws IOException;
  
  public abstract DocumentEntry createDocument(String paramString, int paramInt, POIFSWriterListener paramPOIFSWriterListener)
    throws IOException;
  
  public abstract DirectoryEntry createDirectory(String paramString)
    throws IOException;
  
  public abstract ClassID getStorageClsid();
  
  public abstract void setStorageClsid(ClassID paramClassID);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.DirectoryEntry
 * JD-Core Version:    0.7.0.1
 */