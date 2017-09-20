package org.apache.poi.ss.formula;

public abstract interface ExternSheetReferenceToken
{
  public abstract int getExternSheetIndex();
  
  public abstract String format2DRefAsString();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ExternSheetReferenceToken
 * JD-Core Version:    0.7.0.1
 */