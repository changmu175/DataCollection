package org.apache.poi.ss.usermodel.charts;

import org.apache.poi.ss.util.CellReference;

public abstract interface ChartSeries
{
  public abstract void setTitle(String paramString);
  
  public abstract void setTitle(CellReference paramCellReference);
  
  public abstract String getTitleString();
  
  public abstract CellReference getTitleCellReference();
  
  public abstract TitleType getTitleType();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.charts.ChartSeries
 * JD-Core Version:    0.7.0.1
 */