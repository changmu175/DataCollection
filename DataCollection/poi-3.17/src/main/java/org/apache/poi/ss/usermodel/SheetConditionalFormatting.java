package org.apache.poi.ss.usermodel;

import org.apache.poi.ss.util.CellRangeAddress;

public abstract interface SheetConditionalFormatting
{
  public abstract int addConditionalFormatting(CellRangeAddress[] paramArrayOfCellRangeAddress, ConditionalFormattingRule paramConditionalFormattingRule);
  
  public abstract int addConditionalFormatting(CellRangeAddress[] paramArrayOfCellRangeAddress, ConditionalFormattingRule paramConditionalFormattingRule1, ConditionalFormattingRule paramConditionalFormattingRule2);
  
  public abstract int addConditionalFormatting(CellRangeAddress[] paramArrayOfCellRangeAddress, ConditionalFormattingRule[] paramArrayOfConditionalFormattingRule);
  
  public abstract int addConditionalFormatting(ConditionalFormatting paramConditionalFormatting);
  
  public abstract ConditionalFormattingRule createConditionalFormattingRule(byte paramByte, String paramString1, String paramString2);
  
  public abstract ConditionalFormattingRule createConditionalFormattingRule(byte paramByte, String paramString);
  
  public abstract ConditionalFormattingRule createConditionalFormattingRule(String paramString);
  
  public abstract ConditionalFormattingRule createConditionalFormattingRule(ExtendedColor paramExtendedColor);
  
  public abstract ConditionalFormattingRule createConditionalFormattingRule(IconMultiStateFormatting.IconSet paramIconSet);
  
  public abstract ConditionalFormattingRule createConditionalFormattingColorScaleRule();
  
  public abstract ConditionalFormatting getConditionalFormattingAt(int paramInt);
  
  public abstract int getNumConditionalFormattings();
  
  public abstract void removeConditionalFormatting(int paramInt);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.SheetConditionalFormatting
 * JD-Core Version:    0.7.0.1
 */