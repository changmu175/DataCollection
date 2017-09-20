package org.apache.poi.ss.usermodel;

import org.apache.poi.ss.util.CellAddress;

public abstract interface Comment
{
  public abstract void setVisible(boolean paramBoolean);
  
  public abstract boolean isVisible();
  
  public abstract CellAddress getAddress();
  
  public abstract void setAddress(CellAddress paramCellAddress);
  
  public abstract void setAddress(int paramInt1, int paramInt2);
  
  public abstract int getRow();
  
  public abstract void setRow(int paramInt);
  
  public abstract int getColumn();
  
  public abstract void setColumn(int paramInt);
  
  public abstract String getAuthor();
  
  public abstract void setAuthor(String paramString);
  
  public abstract RichTextString getString();
  
  public abstract void setString(RichTextString paramRichTextString);
  
  public abstract ClientAnchor getClientAnchor();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Comment
 * JD-Core Version:    0.7.0.1
 */