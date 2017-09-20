package org.apache.poi.hssf.record;

public abstract interface CellValueRecordInterface
{
  public abstract int getRow();
  
  public abstract short getColumn();
  
  public abstract void setRow(int paramInt);
  
  public abstract void setColumn(short paramShort);
  
  public abstract void setXFIndex(short paramShort);
  
  public abstract short getXFIndex();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CellValueRecordInterface
 * JD-Core Version:    0.7.0.1
 */