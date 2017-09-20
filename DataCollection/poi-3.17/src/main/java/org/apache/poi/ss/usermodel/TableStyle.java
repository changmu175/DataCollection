package org.apache.poi.ss.usermodel;

public abstract interface TableStyle
{
  public abstract String getName();
  
  public abstract int getIndex();
  
  public abstract boolean isBuiltin();
  
  public abstract DifferentialStyleProvider getStyle(TableStyleType paramTableStyleType);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.TableStyle
 * JD-Core Version:    0.7.0.1
 */