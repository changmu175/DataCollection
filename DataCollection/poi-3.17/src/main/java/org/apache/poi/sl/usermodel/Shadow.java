package org.apache.poi.sl.usermodel;

public abstract interface Shadow<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
{
  public abstract SimpleShape<S, P> getShadowParent();
  
  public abstract double getDistance();
  
  public abstract double getAngle();
  
  public abstract double getBlur();
  
  public abstract PaintStyle.SolidPaint getFillStyle();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.Shadow
 * JD-Core Version:    0.7.0.1
 */