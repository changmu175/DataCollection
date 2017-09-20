package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;

public abstract interface FormulaRenderingWorkbook
{
  public abstract EvaluationWorkbook.ExternalSheet getExternalSheet(int paramInt);
  
  public abstract String getSheetFirstNameByExternSheet(int paramInt);
  
  public abstract String getSheetLastNameByExternSheet(int paramInt);
  
  public abstract String resolveNameXText(NameXPtg paramNameXPtg);
  
  public abstract String getNameText(NamePtg paramNamePtg);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.FormulaRenderingWorkbook
 * JD-Core Version:    0.7.0.1
 */