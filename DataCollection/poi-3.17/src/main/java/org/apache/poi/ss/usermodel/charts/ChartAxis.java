package org.apache.poi.ss.usermodel.charts;

public abstract interface ChartAxis
{
  public abstract long getId();
  
  public abstract AxisPosition getPosition();
  
  public abstract void setPosition(AxisPosition paramAxisPosition);
  
  public abstract String getNumberFormat();
  
  public abstract void setNumberFormat(String paramString);
  
  public abstract boolean isSetLogBase();
  
  public abstract void setLogBase(double paramDouble);
  
  public abstract double getLogBase();
  
  public abstract boolean isSetMinimum();
  
  public abstract double getMinimum();
  
  public abstract void setMinimum(double paramDouble);
  
  public abstract boolean isSetMaximum();
  
  public abstract double getMaximum();
  
  public abstract void setMaximum(double paramDouble);
  
  public abstract AxisOrientation getOrientation();
  
  public abstract void setOrientation(AxisOrientation paramAxisOrientation);
  
  public abstract void setCrosses(AxisCrosses paramAxisCrosses);
  
  public abstract AxisCrosses getCrosses();
  
  public abstract void crossAxis(ChartAxis paramChartAxis);
  
  public abstract boolean isVisible();
  
  public abstract void setVisible(boolean paramBoolean);
  
  public abstract AxisTickMark getMajorTickMark();
  
  public abstract void setMajorTickMark(AxisTickMark paramAxisTickMark);
  
  public abstract AxisTickMark getMinorTickMark();
  
  public abstract void setMinorTickMark(AxisTickMark paramAxisTickMark);
  
  public abstract boolean hasNumberFormat();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.charts.ChartAxis
 * JD-Core Version:    0.7.0.1
 */