package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.eval.ValueEval;

public abstract interface TwoDEval
  extends ValueEval
{
  public abstract ValueEval getValue(int paramInt1, int paramInt2);
  
  public abstract int getWidth();
  
  public abstract int getHeight();
  
  public abstract boolean isRow();
  
  public abstract boolean isColumn();
  
  public abstract TwoDEval getRow(int paramInt);
  
  public abstract TwoDEval getColumn(int paramInt);
  
  public abstract boolean isSubTotal(int paramInt1, int paramInt2);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.TwoDEval
 * JD-Core Version:    0.7.0.1
 */