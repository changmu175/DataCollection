package org.apache.poi.ss.usermodel;

import java.util.Map;

public abstract interface FormulaEvaluator
{
  public abstract void clearAllCachedResultValues();
  
  public abstract void notifySetFormula(Cell paramCell);
  
  public abstract void notifyDeleteCell(Cell paramCell);
  
  public abstract void notifyUpdateCell(Cell paramCell);
  
  public abstract void evaluateAll();
  
  public abstract CellValue evaluate(Cell paramCell);
  
  /**
   * @deprecated
   */
  public abstract int evaluateFormulaCell(Cell paramCell);
  
  public abstract CellType evaluateFormulaCellEnum(Cell paramCell);
  
  public abstract Cell evaluateInCell(Cell paramCell);
  
  public abstract void setupReferencedWorkbooks(Map<String, FormulaEvaluator> paramMap);
  
  public abstract void setIgnoreMissingWorkbooks(boolean paramBoolean);
  
  public abstract void setDebugEvaluationOutputForNextEval(boolean paramBoolean);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.FormulaEvaluator
 * JD-Core Version:    0.7.0.1
 */