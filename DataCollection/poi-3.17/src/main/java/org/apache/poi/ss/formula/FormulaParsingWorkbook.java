package org.apache.poi.ss.formula;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Table;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;

public abstract interface FormulaParsingWorkbook
{
  public abstract EvaluationName getName(String paramString, int paramInt);
  
  public abstract Name createName();
  
  public abstract Table getTable(String paramString);
  
  public abstract Ptg getNameXPtg(String paramString, SheetIdentifier paramSheetIdentifier);
  
  public abstract Ptg get3DReferencePtg(CellReference paramCellReference, SheetIdentifier paramSheetIdentifier);
  
  public abstract Ptg get3DReferencePtg(AreaReference paramAreaReference, SheetIdentifier paramSheetIdentifier);
  
  public abstract int getExternalSheetIndex(String paramString);
  
  public abstract int getExternalSheetIndex(String paramString1, String paramString2);
  
  public abstract SpreadsheetVersion getSpreadsheetVersion();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.FormulaParsingWorkbook
 * JD-Core Version:    0.7.0.1
 */