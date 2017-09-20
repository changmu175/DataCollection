package org.apache.poi.ss.usermodel.charts;

public abstract interface ChartLegend
  extends ManuallyPositionable
{
  public abstract LegendPosition getPosition();
  
  public abstract void setPosition(LegendPosition paramLegendPosition);
  
  public abstract boolean isOverlay();
  
  public abstract void setOverlay(boolean paramBoolean);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.charts.ChartLegend
 * JD-Core Version:    0.7.0.1
 */