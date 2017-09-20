package org.apache.poi.ss.usermodel.charts;

public abstract interface ManualLayout
{
  public abstract void setTarget(LayoutTarget paramLayoutTarget);
  
  public abstract LayoutTarget getTarget();
  
  public abstract void setXMode(LayoutMode paramLayoutMode);
  
  public abstract LayoutMode getXMode();
  
  public abstract void setYMode(LayoutMode paramLayoutMode);
  
  public abstract LayoutMode getYMode();
  
  public abstract double getX();
  
  public abstract void setX(double paramDouble);
  
  public abstract double getY();
  
  public abstract void setY(double paramDouble);
  
  public abstract void setWidthMode(LayoutMode paramLayoutMode);
  
  public abstract LayoutMode getWidthMode();
  
  public abstract void setHeightMode(LayoutMode paramLayoutMode);
  
  public abstract LayoutMode getHeightMode();
  
  public abstract void setWidthRatio(double paramDouble);
  
  public abstract double getWidthRatio();
  
  public abstract void setHeightRatio(double paramDouble);
  
  public abstract double getHeightRatio();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.charts.ManualLayout
 * JD-Core Version:    0.7.0.1
 */