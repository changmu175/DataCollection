package org.apache.poi.ss.formula;

import org.apache.poi.ss.usermodel.CellType;

public abstract interface EvaluationCell
{
  public abstract Object getIdentityKey();
  
  public abstract EvaluationSheet getSheet();
  
  public abstract int getRowIndex();
  
  public abstract int getColumnIndex();
  
  /**
   * @deprecated
   */
  public abstract int getCellType();
  
  /**
   * @deprecated
   */
  public abstract CellType getCellTypeEnum();
  
  public abstract double getNumericCellValue();
  
  public abstract String getStringCellValue();
  
  public abstract boolean getBooleanCellValue();
  
  public abstract int getErrorCellValue();
  
  /**
   * @deprecated
   */
  public abstract int getCachedFormulaResultType();
  
  /**
   * @deprecated
   */
  public abstract CellType getCachedFormulaResultTypeEnum();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.EvaluationCell
 * JD-Core Version:    0.7.0.1
 */