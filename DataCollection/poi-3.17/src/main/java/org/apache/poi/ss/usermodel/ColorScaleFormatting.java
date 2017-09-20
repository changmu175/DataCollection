package org.apache.poi.ss.usermodel;

public abstract interface ColorScaleFormatting
{
  public abstract int getNumControlPoints();
  
  public abstract void setNumControlPoints(int paramInt);
  
  public abstract Color[] getColors();
  
  public abstract void setColors(Color[] paramArrayOfColor);
  
  public abstract ConditionalFormattingThreshold[] getThresholds();
  
  public abstract void setThresholds(ConditionalFormattingThreshold[] paramArrayOfConditionalFormattingThreshold);
  
  public abstract ConditionalFormattingThreshold createThreshold();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.ColorScaleFormatting
 * JD-Core Version:    0.7.0.1
 */