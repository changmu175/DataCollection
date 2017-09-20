package org.apache.poi.openxml4j.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;

public abstract interface ZipEntrySource
  extends Closeable
{
  public abstract Enumeration<? extends ZipEntry> getEntries();
  
  public abstract InputStream getInputStream(ZipEntry paramZipEntry)
    throws IOException;
  
  public abstract void close()
    throws IOException;
  
  public abstract boolean isClosed();
}


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.util.ZipEntrySource
 * JD-Core Version:    0.7.0.1
 */