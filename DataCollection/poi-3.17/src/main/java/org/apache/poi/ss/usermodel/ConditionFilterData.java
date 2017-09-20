package org.apache.poi.ss.usermodel;

public abstract interface ConditionFilterData
{
  public abstract boolean getAboveAverage();
  
  public abstract boolean getBottom();
  
  public abstract boolean getEqualAverage();
  
  public abstract boolean getPercent();
  
  public abstract long getRank();
  
  public abstract int getStdDev();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.ConditionFilterData
 * JD-Core Version:    0.7.0.1
 */