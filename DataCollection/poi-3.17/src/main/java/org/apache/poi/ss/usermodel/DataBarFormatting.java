package org.apache.poi.ss.usermodel;

public abstract interface DataBarFormatting
{
  public abstract boolean isLeftToRight();
  
  public abstract void setLeftToRight(boolean paramBoolean);
  
  public abstract boolean isIconOnly();
  
  public abstract void setIconOnly(boolean paramBoolean);
  
  public abstract int getWidthMin();
  
  public abstract void setWidthMin(int paramInt);
  
  public abstract int getWidthMax();
  
  public abstract void setWidthMax(int paramInt);
  
  public abstract Color getColor();
  
  public abstract void setColor(Color paramColor);
  
  public abstract ConditionalFormattingThreshold getMinThreshold();
  
  public abstract ConditionalFormattingThreshold getMaxThreshold();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.DataBarFormatting
 * JD-Core Version:    0.7.0.1
 */