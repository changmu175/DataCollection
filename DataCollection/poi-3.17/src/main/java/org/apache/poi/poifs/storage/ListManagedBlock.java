package org.apache.poi.poifs.storage;

import java.io.IOException;

public abstract interface ListManagedBlock
{
  public abstract byte[] getData()
    throws IOException;
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.ListManagedBlock
 * JD-Core Version:    0.7.0.1
 */