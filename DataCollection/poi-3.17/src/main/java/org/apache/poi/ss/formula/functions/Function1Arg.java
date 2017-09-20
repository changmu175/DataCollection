package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ValueEval;

public abstract interface Function1Arg
  extends Function
{
  public abstract ValueEval evaluate(int paramInt1, int paramInt2, ValueEval paramValueEval);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Function1Arg
 * JD-Core Version:    0.7.0.1
 */