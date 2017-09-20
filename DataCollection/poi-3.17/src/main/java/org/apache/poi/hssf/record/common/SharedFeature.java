package org.apache.poi.hssf.record.common;

import org.apache.poi.util.LittleEndianOutput;

public abstract interface SharedFeature
{
  public abstract String toString();
  
  public abstract void serialize(LittleEndianOutput paramLittleEndianOutput);
  
  public abstract int getDataSize();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.common.SharedFeature
 * JD-Core Version:    0.7.0.1
 */