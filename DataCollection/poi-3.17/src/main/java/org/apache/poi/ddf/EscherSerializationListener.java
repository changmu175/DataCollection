package org.apache.poi.ddf;

public abstract interface EscherSerializationListener
{
  public abstract void beforeRecordSerialize(int paramInt, short paramShort, EscherRecord paramEscherRecord);
  
  public abstract void afterRecordSerialize(int paramInt1, short paramShort, int paramInt2, EscherRecord paramEscherRecord);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherSerializationListener
 * JD-Core Version:    0.7.0.1
 */