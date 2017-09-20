package org.apache.poi.util;

public abstract interface LittleEndianOutput
{
  public abstract void writeByte(int paramInt);
  
  public abstract void writeShort(int paramInt);
  
  public abstract void writeInt(int paramInt);
  
  public abstract void writeLong(long paramLong);
  
  public abstract void writeDouble(double paramDouble);
  
  public abstract void write(byte[] paramArrayOfByte);
  
  public abstract void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.LittleEndianOutput
 * JD-Core Version:    0.7.0.1
 */