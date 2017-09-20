package org.apache.poi.ss.formula;

import org.apache.poi.util.Internal;

@Internal
public abstract interface EvaluationSheet
{
  public abstract EvaluationCell getCell(int paramInt1, int paramInt2);
  
  public abstract void clearAllCachedResultValues();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.EvaluationSheet
 * JD-Core Version:    0.7.0.1
 */