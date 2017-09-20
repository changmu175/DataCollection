package org.apache.poi.ss.usermodel;

public abstract interface TableStyleInfo
{
  public abstract boolean isShowColumnStripes();
  
  public abstract boolean isShowRowStripes();
  
  public abstract boolean isShowFirstColumn();
  
  public abstract boolean isShowLastColumn();
  
  public abstract String getName();
  
  public abstract TableStyle getStyle();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.TableStyleInfo
 * JD-Core Version:    0.7.0.1
 */