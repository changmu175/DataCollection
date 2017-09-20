package org.apache.poi.util;

public abstract interface DelayableLittleEndianOutput
  extends LittleEndianOutput
{
  public abstract LittleEndianOutput createDelayedOutput(int paramInt);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.DelayableLittleEndianOutput
 * JD-Core Version:    0.7.0.1
 */