package org.apache.poi.sl.draw.geom;

public abstract interface Formula
{
  public static final double OOXML_DEGREE = 60000.0D;
  
  public abstract double evaluate(Context paramContext);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.Formula
 * JD-Core Version:    0.7.0.1
 */