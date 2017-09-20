package org.apache.poi.ss.usermodel.charts;

public abstract interface ChartDataSource<T>
{
  public abstract int getPointCount();
  
  public abstract T getPointAt(int paramInt);
  
  public abstract boolean isReference();
  
  public abstract boolean isNumeric();
  
  public abstract String getFormulaString();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.charts.ChartDataSource
 * JD-Core Version:    0.7.0.1
 */