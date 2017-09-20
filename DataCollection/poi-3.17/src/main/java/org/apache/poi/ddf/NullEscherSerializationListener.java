package org.apache.poi.ddf;

public class NullEscherSerializationListener
  implements EscherSerializationListener
{
  public void beforeRecordSerialize(int offset, short recordId, EscherRecord record) {}
  
  public void afterRecordSerialize(int offset, short recordId, int size, EscherRecord record) {}
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.NullEscherSerializationListener
 * JD-Core Version:    0.7.0.1
 */