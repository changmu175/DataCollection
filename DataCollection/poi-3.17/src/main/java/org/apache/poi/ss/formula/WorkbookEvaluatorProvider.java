package org.apache.poi.ss.formula;

import org.apache.poi.util.Internal;

@Internal
public abstract interface WorkbookEvaluatorProvider
{
  public abstract WorkbookEvaluator _getWorkbookEvaluator();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.WorkbookEvaluatorProvider
 * JD-Core Version:    0.7.0.1
 */