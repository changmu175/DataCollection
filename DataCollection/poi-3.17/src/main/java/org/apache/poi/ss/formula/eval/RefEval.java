package org.apache.poi.ss.formula.eval;

import org.apache.poi.ss.formula.SheetRange;

public abstract interface RefEval
  extends ValueEval, SheetRange
{
  public abstract ValueEval getInnerValueEval(int paramInt);
  
  public abstract int getColumn();
  
  public abstract int getRow();
  
  public abstract int getFirstSheetIndex();
  
  public abstract int getLastSheetIndex();
  
  public abstract int getNumberOfSheets();
  
  public abstract AreaEval offset(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.RefEval
 * JD-Core Version:    0.7.0.1
 */