package org.apache.poi.hssf.eventusermodel;

import org.apache.poi.hssf.record.Record;

public abstract class AbortableHSSFListener
  implements HSSFListener
{
  public void processRecord(Record record) {}
  
  public abstract short abortableProcessRecord(Record paramRecord)
    throws HSSFUserException;
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.eventusermodel.AbortableHSSFListener
 * JD-Core Version:    0.7.0.1
 */