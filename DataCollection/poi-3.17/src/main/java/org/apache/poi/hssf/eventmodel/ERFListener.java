package org.apache.poi.hssf.eventmodel;

import org.apache.poi.hssf.record.Record;

public abstract interface ERFListener
{
  public abstract boolean processRecord(Record paramRecord);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.eventmodel.ERFListener
 * JD-Core Version:    0.7.0.1
 */