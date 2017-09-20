package org.apache.poi.hssf.record.common;

import org.apache.poi.ss.util.CellRangeAddress;

public abstract interface FutureRecord
{
  public abstract short getFutureRecordType();
  
  public abstract FtrHeader getFutureHeader();
  
  public abstract CellRangeAddress getAssociatedRange();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.common.FutureRecord
 * JD-Core Version:    0.7.0.1
 */