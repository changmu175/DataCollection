package org.apache.poi.ss.usermodel;

public abstract interface FontFormatting
{
  public static final short SS_NONE = 0;
  public static final short SS_SUPER = 1;
  public static final short SS_SUB = 2;
  public static final byte U_NONE = 0;
  public static final byte U_SINGLE = 1;
  public static final byte U_DOUBLE = 2;
  public static final byte U_SINGLE_ACCOUNTING = 33;
  public static final byte U_DOUBLE_ACCOUNTING = 34;
  
  public abstract short getEscapementType();
  
  public abstract void setEscapementType(short paramShort);
  
  public abstract short getFontColorIndex();
  
  public abstract void setFontColorIndex(short paramShort);
  
  public abstract Color getFontColor();
  
  public abstract void setFontColor(Color paramColor);
  
  public abstract int getFontHeight();
  
  public abstract void setFontHeight(int paramInt);
  
  public abstract short getUnderlineType();
  
  public abstract void setUnderlineType(short paramShort);
  
  public abstract boolean isBold();
  
  public abstract boolean isItalic();
  
  public abstract boolean isStruckout();
  
  public abstract void setFontStyle(boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void resetFontStyle();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.FontFormatting
 * JD-Core Version:    0.7.0.1
 */