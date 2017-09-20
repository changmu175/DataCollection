package org.apache.poi.ss.usermodel;

public abstract interface DifferentialStyleProvider
{
  public abstract BorderFormatting getBorderFormatting();
  
  public abstract FontFormatting getFontFormatting();
  
  public abstract ExcelNumberFormat getNumberFormat();
  
  public abstract PatternFormatting getPatternFormatting();
  
  public abstract int getStripeSize();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.DifferentialStyleProvider
 * JD-Core Version:    0.7.0.1
 */