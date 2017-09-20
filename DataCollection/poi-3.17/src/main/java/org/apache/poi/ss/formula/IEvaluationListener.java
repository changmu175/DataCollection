package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.eval.ValueEval;

abstract interface IEvaluationListener
{
  public abstract void onCacheHit(int paramInt1, int paramInt2, int paramInt3, ValueEval paramValueEval);
  
  public abstract void onReadPlainValue(int paramInt1, int paramInt2, int paramInt3, ICacheEntry paramICacheEntry);
  
  public abstract void onStartEvaluate(EvaluationCell paramEvaluationCell, ICacheEntry paramICacheEntry);
  
  public abstract void onEndEvaluate(ICacheEntry paramICacheEntry, ValueEval paramValueEval);
  
  public abstract void onClearWholeCache();
  
  public abstract void onClearCachedValue(ICacheEntry paramICacheEntry);
  
  public abstract void sortDependentCachedValues(ICacheEntry[] paramArrayOfICacheEntry);
  
  public abstract void onClearDependentCachedValue(ICacheEntry paramICacheEntry, int paramInt);
  
  public abstract void onChangeFromBlankValue(int paramInt1, int paramInt2, int paramInt3, EvaluationCell paramEvaluationCell, ICacheEntry paramICacheEntry);
  
  public static abstract interface ICacheEntry
  {
    public abstract ValueEval getValue();
  }
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.IEvaluationListener
 * JD-Core Version:    0.7.0.1
 */