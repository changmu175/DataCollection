package org.apache.poi.ss.usermodel.charts;

public abstract interface LineChartSeries
  extends ChartSeries
{
  public abstract ChartDataSource<?> getCategoryAxisData();
  
  public abstract ChartDataSource<? extends Number> getValues();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.charts.LineChartSeries
 * JD-Core Version:    0.7.0.1
 */