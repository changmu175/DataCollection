package org.apache.poi.poifs.storage;

import java.io.IOException;
import java.io.OutputStream;

public abstract interface BlockWritable
{
  public abstract void writeBlocks(OutputStream paramOutputStream)
    throws IOException;
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.BlockWritable
 * JD-Core Version:    0.7.0.1
 */