package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.eval.ValueEval;

public abstract interface ThreeDEval
  extends TwoDEval, SheetRange
{
  public abstract ValueEval getValue(int paramInt1, int paramInt2, int paramInt3);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ThreeDEval
 * JD-Core Version:    0.7.0.1
 */