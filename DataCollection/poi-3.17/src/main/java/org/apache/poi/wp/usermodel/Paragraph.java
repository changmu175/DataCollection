package org.apache.poi.wp.usermodel;

public abstract interface Paragraph
{
  public abstract int getIndentFromRight();
  
  public abstract void setIndentFromRight(int paramInt);
  
  public abstract int getIndentFromLeft();
  
  public abstract void setIndentFromLeft(int paramInt);
  
  public abstract int getFirstLineIndent();
  
  public abstract void setFirstLineIndent(int paramInt);
  
  public abstract int getFontAlignment();
  
  public abstract void setFontAlignment(int paramInt);
  
  public abstract boolean isWordWrapped();
  
  public abstract void setWordWrapped(boolean paramBoolean);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.wp.usermodel.Paragraph
 * JD-Core Version:    0.7.0.1
 */