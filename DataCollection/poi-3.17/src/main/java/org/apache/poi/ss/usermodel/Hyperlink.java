package org.apache.poi.ss.usermodel;

public abstract interface Hyperlink
  extends org.apache.poi.common.usermodel.Hyperlink
{
  public abstract int getFirstRow();
  
  public abstract void setFirstRow(int paramInt);
  
  public abstract int getLastRow();
  
  public abstract void setLastRow(int paramInt);
  
  public abstract int getFirstColumn();
  
  public abstract void setFirstColumn(int paramInt);
  
  public abstract int getLastColumn();
  
  public abstract void setLastColumn(int paramInt);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Hyperlink
 * JD-Core Version:    0.7.0.1
 */