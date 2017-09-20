package org.apache.poi.util;

import java.io.IOException;
import java.io.InputStream;

public abstract interface FixedField
{
  public abstract void readFromBytes(byte[] paramArrayOfByte)
    throws ArrayIndexOutOfBoundsException;
  
  public abstract void readFromStream(InputStream paramInputStream)
    throws IOException;
  
  public abstract void writeToBytes(byte[] paramArrayOfByte)
    throws ArrayIndexOutOfBoundsException;
  
  public abstract String toString();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.FixedField
 * JD-Core Version:    0.7.0.1
 */