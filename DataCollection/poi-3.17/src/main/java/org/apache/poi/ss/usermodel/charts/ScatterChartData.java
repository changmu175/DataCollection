package org.apache.poi.ss.usermodel.charts;

import java.util.List;

public abstract interface ScatterChartData
  extends ChartData
{
  public abstract ScatterChartSeries addSerie(ChartDataSource<?> paramChartDataSource, ChartDataSource<? extends Number> paramChartDataSource1);
  
  public abstract List<? extends ScatterChartSeries> getSeries();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.charts.ScatterChartData
 * JD-Core Version:    0.7.0.1
 */