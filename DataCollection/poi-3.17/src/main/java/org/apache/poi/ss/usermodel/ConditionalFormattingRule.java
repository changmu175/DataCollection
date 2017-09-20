package org.apache.poi.ss.usermodel;

public abstract interface ConditionalFormattingRule
  extends DifferentialStyleProvider
{
  public abstract BorderFormatting createBorderFormatting();
  
  public abstract BorderFormatting getBorderFormatting();
  
  public abstract FontFormatting createFontFormatting();
  
  public abstract FontFormatting getFontFormatting();
  
  public abstract PatternFormatting createPatternFormatting();
  
  public abstract PatternFormatting getPatternFormatting();
  
  public abstract DataBarFormatting getDataBarFormatting();
  
  public abstract IconMultiStateFormatting getMultiStateFormatting();
  
  public abstract ColorScaleFormatting getColorScaleFormatting();
  
  public abstract ExcelNumberFormat getNumberFormat();
  
  public abstract ConditionType getConditionType();
  
  public abstract ConditionFilterType getConditionFilterType();
  
  public abstract ConditionFilterData getFilterConfiguration();
  
  public abstract byte getComparisonOperation();
  
  public abstract String getFormula1();
  
  public abstract String getFormula2();
  
  public abstract int getPriority();
  
  public abstract boolean getStopIfTrue();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.ConditionalFormattingRule
 * JD-Core Version:    0.7.0.1
 */