package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ValueEval;

public abstract interface Function
{
  public abstract ValueEval evaluate(ValueEval[] paramArrayOfValueEval, int paramInt1, int paramInt2);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Function
 * JD-Core Version:    0.7.0.1
 */