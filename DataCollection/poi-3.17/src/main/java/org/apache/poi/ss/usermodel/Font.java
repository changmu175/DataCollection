package org.apache.poi.ss.usermodel;

public abstract interface Font
{
  public static final short COLOR_NORMAL = 32767;
  public static final short COLOR_RED = 10;
  public static final short SS_NONE = 0;
  public static final short SS_SUPER = 1;
  public static final short SS_SUB = 2;
  public static final byte U_NONE = 0;
  public static final byte U_SINGLE = 1;
  public static final byte U_DOUBLE = 2;
  public static final byte U_SINGLE_ACCOUNTING = 33;
  public static final byte U_DOUBLE_ACCOUNTING = 34;
  public static final byte ANSI_CHARSET = 0;
  public static final byte DEFAULT_CHARSET = 1;
  public static final byte SYMBOL_CHARSET = 2;
  
  public abstract void setFontName(String paramString);
  
  public abstract String getFontName();
  
  public abstract void setFontHeight(short paramShort);
  
  public abstract void setFontHeightInPoints(short paramShort);
  
  public abstract short getFontHeight();
  
  public abstract short getFontHeightInPoints();
  
  public abstract void setItalic(boolean paramBoolean);
  
  public abstract boolean getItalic();
  
  public abstract void setStrikeout(boolean paramBoolean);
  
  public abstract boolean getStrikeout();
  
  public abstract void setColor(short paramShort);
  
  public abstract short getColor();
  
  public abstract void setTypeOffset(short paramShort);
  
  public abstract short getTypeOffset();
  
  public abstract void setUnderline(byte paramByte);
  
  public abstract byte getUnderline();
  
  public abstract int getCharSet();
  
  public abstract void setCharSet(byte paramByte);
  
  public abstract void setCharSet(int paramInt);
  
  public abstract short getIndex();
  
  public abstract void setBold(boolean paramBoolean);
  
  public abstract boolean getBold();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Font
 * JD-Core Version:    0.7.0.1
 */