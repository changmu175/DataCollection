package org.apache.poi.ss.usermodel.charts;

public abstract interface ChartAxisFactory
{
  public abstract ValueAxis createValueAxis(AxisPosition paramAxisPosition);
  
  public abstract ChartAxis createCategoryAxis(AxisPosition paramAxisPosition);
  
  public abstract ChartAxis createDateAxis(AxisPosition paramAxisPosition);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.charts.ChartAxisFactory
 * JD-Core Version:    0.7.0.1
 */