package org.apache.poi.hssf.record;

public abstract class RecordBase
{
  public abstract int serialize(int paramInt, byte[] paramArrayOfByte);
  
  public abstract int getRecordSize();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.RecordBase
 * JD-Core Version:    0.7.0.1
 */