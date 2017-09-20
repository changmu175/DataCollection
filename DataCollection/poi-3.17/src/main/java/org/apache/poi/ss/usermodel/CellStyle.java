package org.apache.poi.ss.usermodel;

public abstract interface CellStyle
{
  public abstract short getIndex();
  
  public abstract void setDataFormat(short paramShort);
  
  public abstract short getDataFormat();
  
  public abstract String getDataFormatString();
  
  public abstract void setFont(Font paramFont);
  
  public abstract short getFontIndex();
  
  public abstract void setHidden(boolean paramBoolean);
  
  public abstract boolean getHidden();
  
  public abstract void setLocked(boolean paramBoolean);
  
  public abstract boolean getLocked();
  
  public abstract void setQuotePrefixed(boolean paramBoolean);
  
  public abstract boolean getQuotePrefixed();
  
  public abstract void setAlignment(HorizontalAlignment paramHorizontalAlignment);
  
  /**
   * @deprecated
   */
  public abstract short getAlignment();
  
  public abstract HorizontalAlignment getAlignmentEnum();
  
  public abstract void setWrapText(boolean paramBoolean);
  
  public abstract boolean getWrapText();
  
  public abstract void setVerticalAlignment(VerticalAlignment paramVerticalAlignment);
  
  /**
   * @deprecated
   */
  public abstract short getVerticalAlignment();
  
  public abstract VerticalAlignment getVerticalAlignmentEnum();
  
  public abstract void setRotation(short paramShort);
  
  public abstract short getRotation();
  
  public abstract void setIndention(short paramShort);
  
  public abstract short getIndention();
  
  public abstract void setBorderLeft(BorderStyle paramBorderStyle);
  
  /**
   * @deprecated
   */
  public abstract short getBorderLeft();
  
  public abstract BorderStyle getBorderLeftEnum();
  
  public abstract void setBorderRight(BorderStyle paramBorderStyle);
  
  /**
   * @deprecated
   */
  public abstract short getBorderRight();
  
  public abstract BorderStyle getBorderRightEnum();
  
  public abstract void setBorderTop(BorderStyle paramBorderStyle);
  
  /**
   * @deprecated
   */
  public abstract short getBorderTop();
  
  public abstract BorderStyle getBorderTopEnum();
  
  public abstract void setBorderBottom(BorderStyle paramBorderStyle);
  
  /**
   * @deprecated
   */
  public abstract short getBorderBottom();
  
  public abstract BorderStyle getBorderBottomEnum();
  
  public abstract void setLeftBorderColor(short paramShort);
  
  public abstract short getLeftBorderColor();
  
  public abstract void setRightBorderColor(short paramShort);
  
  public abstract short getRightBorderColor();
  
  public abstract void setTopBorderColor(short paramShort);
  
  public abstract short getTopBorderColor();
  
  public abstract void setBottomBorderColor(short paramShort);
  
  public abstract short getBottomBorderColor();
  
  public abstract void setFillPattern(FillPatternType paramFillPatternType);
  
  /**
   * @deprecated
   */
  public abstract short getFillPattern();
  
  public abstract FillPatternType getFillPatternEnum();
  
  public abstract void setFillBackgroundColor(short paramShort);
  
  public abstract short getFillBackgroundColor();
  
  public abstract Color getFillBackgroundColorColor();
  
  public abstract void setFillForegroundColor(short paramShort);
  
  public abstract short getFillForegroundColor();
  
  public abstract Color getFillForegroundColorColor();
  
  public abstract void cloneStyleFrom(CellStyle paramCellStyle);
  
  public abstract void setShrinkToFit(boolean paramBoolean);
  
  public abstract boolean getShrinkToFit();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.CellStyle
 * JD-Core Version:    0.7.0.1
 */