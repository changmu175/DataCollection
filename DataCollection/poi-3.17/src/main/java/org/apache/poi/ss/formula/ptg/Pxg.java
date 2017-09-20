package org.apache.poi.ss.formula.ptg;

public abstract interface Pxg
{
  public abstract int getExternalWorkbookNumber();
  
  public abstract String getSheetName();
  
  public abstract void setSheetName(String paramString);
  
  public abstract String toFormulaString();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.Pxg
 * JD-Core Version:    0.7.0.1
 */