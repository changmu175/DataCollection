package org.apache.poi.ss.usermodel.charts;

public abstract interface ValueAxis
  extends ChartAxis
{
  public abstract AxisCrossBetween getCrossBetween();
  
  public abstract void setCrossBetween(AxisCrossBetween paramAxisCrossBetween);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.charts.ValueAxis
 * JD-Core Version:    0.7.0.1
 */