package org.apache.poi.ss.usermodel;

import org.apache.poi.ss.util.CellRangeAddress;

public abstract interface ConditionalFormatting
{
  public abstract CellRangeAddress[] getFormattingRanges();
  
  public abstract void setFormattingRanges(CellRangeAddress[] paramArrayOfCellRangeAddress);
  
  public abstract void setRule(int paramInt, ConditionalFormattingRule paramConditionalFormattingRule);
  
  public abstract void addRule(ConditionalFormattingRule paramConditionalFormattingRule);
  
  public abstract ConditionalFormattingRule getRule(int paramInt);
  
  public abstract int getNumberOfRules();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.ConditionalFormatting
 * JD-Core Version:    0.7.0.1
 */