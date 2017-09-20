package org.apache.poi.util;

import java.io.File;
import java.io.IOException;

public abstract interface TempFileCreationStrategy
{
  public abstract File createTempFile(String paramString1, String paramString2)
    throws IOException;
  
  public abstract File createTempDirectory(String paramString)
    throws IOException;
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.TempFileCreationStrategy
 * JD-Core Version:    0.7.0.1
 */