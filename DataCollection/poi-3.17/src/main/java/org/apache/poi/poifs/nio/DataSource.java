package org.apache.poi.poifs.nio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public abstract class DataSource
{
  public abstract ByteBuffer read(int paramInt, long paramLong)
    throws IOException;
  
  public abstract void write(ByteBuffer paramByteBuffer, long paramLong)
    throws IOException;
  
  public abstract long size()
    throws IOException;
  
  public abstract void close()
    throws IOException;
  
  public abstract void copyTo(OutputStream paramOutputStream)
    throws IOException;
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.nio.DataSource
 * JD-Core Version:    0.7.0.1
 */