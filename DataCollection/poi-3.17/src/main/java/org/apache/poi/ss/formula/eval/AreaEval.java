package org.apache.poi.ss.formula.eval;

import org.apache.poi.ss.formula.ThreeDEval;
import org.apache.poi.ss.formula.TwoDEval;

public abstract interface AreaEval
  extends TwoDEval, ThreeDEval
{
  public abstract int getFirstRow();
  
  public abstract int getLastRow();
  
  public abstract int getFirstColumn();
  
  public abstract int getLastColumn();
  
  public abstract ValueEval getAbsoluteValue(int paramInt1, int paramInt2);
  
  public abstract boolean contains(int paramInt1, int paramInt2);
  
  public abstract boolean containsColumn(int paramInt);
  
  public abstract boolean containsRow(int paramInt);
  
  public abstract int getWidth();
  
  public abstract int getHeight();
  
  public abstract ValueEval getRelativeValue(int paramInt1, int paramInt2);
  
  public abstract AreaEval offset(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.AreaEval
 * JD-Core Version:    0.7.0.1
 */