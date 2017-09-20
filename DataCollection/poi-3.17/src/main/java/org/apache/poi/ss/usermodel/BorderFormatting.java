package org.apache.poi.ss.usermodel;

public abstract interface BorderFormatting
{
  /**
   * @deprecated
   */
  public abstract short getBorderBottom();
  
  public abstract BorderStyle getBorderBottomEnum();
  
  /**
   * @deprecated
   */
  public abstract short getBorderDiagonal();
  
  public abstract BorderStyle getBorderDiagonalEnum();
  
  /**
   * @deprecated
   */
  public abstract short getBorderLeft();
  
  public abstract BorderStyle getBorderLeftEnum();
  
  /**
   * @deprecated
   */
  public abstract short getBorderRight();
  
  public abstract BorderStyle getBorderRightEnum();
  
  /**
   * @deprecated
   */
  public abstract short getBorderTop();
  
  public abstract BorderStyle getBorderTopEnum();
  
  public abstract BorderStyle getBorderVerticalEnum();
  
  public abstract BorderStyle getBorderHorizontalEnum();
  
  public abstract short getBottomBorderColor();
  
  public abstract Color getBottomBorderColorColor();
  
  public abstract short getDiagonalBorderColor();
  
  public abstract Color getDiagonalBorderColorColor();
  
  public abstract short getLeftBorderColor();
  
  public abstract Color getLeftBorderColorColor();
  
  public abstract short getRightBorderColor();
  
  public abstract Color getRightBorderColorColor();
  
  public abstract short getTopBorderColor();
  
  public abstract Color getTopBorderColorColor();
  
  public abstract short getVerticalBorderColor();
  
  public abstract Color getVerticalBorderColorColor();
  
  public abstract short getHorizontalBorderColor();
  
  public abstract Color getHorizontalBorderColorColor();
  
  /**
   * @deprecated
   */
  public abstract void setBorderBottom(short paramShort);
  
  public abstract void setBorderBottom(BorderStyle paramBorderStyle);
  
  /**
   * @deprecated
   */
  public abstract void setBorderDiagonal(short paramShort);
  
  public abstract void setBorderDiagonal(BorderStyle paramBorderStyle);
  
  /**
   * @deprecated
   */
  public abstract void setBorderLeft(short paramShort);
  
  public abstract void setBorderLeft(BorderStyle paramBorderStyle);
  
  /**
   * @deprecated
   */
  public abstract void setBorderRight(short paramShort);
  
  public abstract void setBorderRight(BorderStyle paramBorderStyle);
  
  /**
   * @deprecated
   */
  public abstract void setBorderTop(short paramShort);
  
  public abstract void setBorderTop(BorderStyle paramBorderStyle);
  
  public abstract void setBorderHorizontal(BorderStyle paramBorderStyle);
  
  public abstract void setBorderVertical(BorderStyle paramBorderStyle);
  
  public abstract void setBottomBorderColor(short paramShort);
  
  public abstract void setBottomBorderColor(Color paramColor);
  
  public abstract void setDiagonalBorderColor(short paramShort);
  
  public abstract void setDiagonalBorderColor(Color paramColor);
  
  public abstract void setLeftBorderColor(short paramShort);
  
  public abstract void setLeftBorderColor(Color paramColor);
  
  public abstract void setRightBorderColor(short paramShort);
  
  public abstract void setRightBorderColor(Color paramColor);
  
  public abstract void setTopBorderColor(short paramShort);
  
  public abstract void setTopBorderColor(Color paramColor);
  
  public abstract void setHorizontalBorderColor(short paramShort);
  
  public abstract void setHorizontalBorderColor(Color paramColor);
  
  public abstract void setVerticalBorderColor(short paramShort);
  
  public abstract void setVerticalBorderColor(Color paramColor);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.BorderFormatting
 * JD-Core Version:    0.7.0.1
 */