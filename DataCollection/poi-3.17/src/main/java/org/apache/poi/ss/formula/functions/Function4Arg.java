package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ValueEval;

public abstract interface Function4Arg
  extends Function
{
  public abstract ValueEval evaluate(int paramInt1, int paramInt2, ValueEval paramValueEval1, ValueEval paramValueEval2, ValueEval paramValueEval3, ValueEval paramValueEval4);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Function4Arg
 * JD-Core Version:    0.7.0.1
 */