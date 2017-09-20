package org.apache.poi.hssf.eventusermodel;

import org.apache.poi.hssf.record.Record;

public abstract interface HSSFListener
{
  public abstract void processRecord(Record paramRecord);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.eventusermodel.HSSFListener
 * JD-Core Version:    0.7.0.1
 */