package org.apache.poi.ss.usermodel.charts;

public abstract interface ScatterChartSeries
  extends ChartSeries
{
  public abstract ChartDataSource<?> getXValues();
  
  public abstract ChartDataSource<? extends Number> getYValues();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.charts.ScatterChartSeries
 * JD-Core Version:    0.7.0.1
 */