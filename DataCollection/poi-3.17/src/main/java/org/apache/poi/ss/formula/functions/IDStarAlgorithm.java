package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ValueEval;

public abstract interface IDStarAlgorithm
{
  public abstract boolean processMatch(ValueEval paramValueEval);
  
  public abstract ValueEval getResult();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.IDStarAlgorithm
 * JD-Core Version:    0.7.0.1
 */