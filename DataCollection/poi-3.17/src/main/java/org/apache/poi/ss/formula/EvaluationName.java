package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.Ptg;

public abstract interface EvaluationName
{
  public abstract String getNameText();
  
  public abstract boolean isFunctionName();
  
  public abstract boolean hasFormula();
  
  public abstract Ptg[] getNameDefinition();
  
  public abstract boolean isRange();
  
  public abstract NamePtg createPtg();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.EvaluationName
 * JD-Core Version:    0.7.0.1
 */