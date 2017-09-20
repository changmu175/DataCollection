package org.apache.poi.ss.usermodel;

import org.apache.poi.util.Removal;

@Deprecated
@Removal(version="3.18")
public abstract interface Textbox
{
  public static final short OBJECT_TYPE_TEXT = 6;
  
  public abstract RichTextString getString();
  
  public abstract void setString(RichTextString paramRichTextString);
  
  public abstract int getMarginLeft();
  
  public abstract void setMarginLeft(int paramInt);
  
  public abstract int getMarginRight();
  
  public abstract void setMarginRight(int paramInt);
  
  public abstract int getMarginTop();
  
  public abstract void setMarginTop(int paramInt);
  
  public abstract int getMarginBottom();
  
  public abstract void setMarginBottom(int paramInt);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Textbox
 * JD-Core Version:    0.7.0.1
 */