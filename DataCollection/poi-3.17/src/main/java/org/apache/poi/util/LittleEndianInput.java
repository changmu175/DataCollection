package org.apache.poi.util;

public abstract interface LittleEndianInput
{
  public abstract int available();
  
  public abstract byte readByte();
  
  public abstract int readUByte();
  
  public abstract short readShort();
  
  public abstract int readUShort();
  
  public abstract int readInt();
  
  public abstract long readLong();
  
  public abstract double readDouble();
  
  public abstract void readFully(byte[] paramArrayOfByte);
  
  public abstract void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public abstract void readPlain(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.LittleEndianInput
 * JD-Core Version:    0.7.0.1
 */