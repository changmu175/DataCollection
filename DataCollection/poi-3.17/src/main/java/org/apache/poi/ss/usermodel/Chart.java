package org.apache.poi.ss.usermodel;

import java.util.List;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartAxisFactory;
import org.apache.poi.ss.usermodel.charts.ChartData;
import org.apache.poi.ss.usermodel.charts.ChartDataFactory;
import org.apache.poi.ss.usermodel.charts.ChartLegend;
import org.apache.poi.ss.usermodel.charts.ManuallyPositionable;

public abstract interface Chart
  extends ManuallyPositionable
{
  public abstract ChartDataFactory getChartDataFactory();
  
  public abstract ChartAxisFactory getChartAxisFactory();
  
  public abstract ChartLegend getOrCreateLegend();
  
  public abstract void deleteLegend();
  
  public abstract List<? extends ChartAxis> getAxis();
  
  public abstract void plot(ChartData paramChartData, ChartAxis... paramVarArgs);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Chart
 * JD-Core Version:    0.7.0.1
 */