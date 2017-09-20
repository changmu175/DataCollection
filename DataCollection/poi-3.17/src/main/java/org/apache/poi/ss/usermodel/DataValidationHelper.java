package org.apache.poi.ss.usermodel;

import org.apache.poi.ss.util.CellRangeAddressList;

public abstract interface DataValidationHelper
{
  public abstract DataValidationConstraint createFormulaListConstraint(String paramString);
  
  public abstract DataValidationConstraint createExplicitListConstraint(String[] paramArrayOfString);
  
  public abstract DataValidationConstraint createNumericConstraint(int paramInt1, int paramInt2, String paramString1, String paramString2);
  
  public abstract DataValidationConstraint createTextLengthConstraint(int paramInt, String paramString1, String paramString2);
  
  public abstract DataValidationConstraint createDecimalConstraint(int paramInt, String paramString1, String paramString2);
  
  public abstract DataValidationConstraint createIntegerConstraint(int paramInt, String paramString1, String paramString2);
  
  public abstract DataValidationConstraint createDateConstraint(int paramInt, String paramString1, String paramString2, String paramString3);
  
  public abstract DataValidationConstraint createTimeConstraint(int paramInt, String paramString1, String paramString2);
  
  public abstract DataValidationConstraint createCustomConstraint(String paramString);
  
  public abstract DataValidation createValidation(DataValidationConstraint paramDataValidationConstraint, CellRangeAddressList paramCellRangeAddressList);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.DataValidationHelper
 * JD-Core Version:    0.7.0.1
 */