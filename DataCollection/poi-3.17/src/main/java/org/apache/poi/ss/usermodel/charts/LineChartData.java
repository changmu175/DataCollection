package org.apache.poi.ss.usermodel.charts;

import java.util.List;

public abstract interface LineChartData
  extends ChartData
{
  public abstract LineChartSeries addSeries(ChartDataSource<?> paramChartDataSource, ChartDataSource<? extends Number> paramChartDataSource1);
  
  public abstract List<? extends LineChartSeries> getSeries();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.charts.LineChartData
 * JD-Core Version:    0.7.0.1
 */