package org.apache.poi.hssf.record;

public abstract interface BiffHeaderInput
{
  public abstract int readRecordSID();
  
  public abstract int readDataSize();
  
  public abstract int available();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.BiffHeaderInput
 * JD-Core Version:    0.7.0.1
 */