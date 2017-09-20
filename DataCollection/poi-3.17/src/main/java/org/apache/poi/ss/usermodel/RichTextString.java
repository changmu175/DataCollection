package org.apache.poi.ss.usermodel;

public abstract interface RichTextString
{
  public abstract void applyFont(int paramInt1, int paramInt2, short paramShort);
  
  public abstract void applyFont(int paramInt1, int paramInt2, Font paramFont);
  
  public abstract void applyFont(Font paramFont);
  
  public abstract void clearFormatting();
  
  public abstract String getString();
  
  public abstract int length();
  
  public abstract int numFormattingRuns();
  
  public abstract int getIndexOfFormattingRun(int paramInt);
  
  public abstract void applyFont(short paramShort);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.RichTextString
 * JD-Core Version:    0.7.0.1
 */