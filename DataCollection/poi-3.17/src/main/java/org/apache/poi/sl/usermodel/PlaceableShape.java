package org.apache.poi.sl.usermodel;

import java.awt.geom.Rectangle2D;

public abstract interface PlaceableShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
{
  public abstract ShapeContainer<S, P> getParent();
  
  public abstract Sheet<S, P> getSheet();
  
  public abstract Rectangle2D getAnchor();
  
  public abstract void setAnchor(Rectangle2D paramRectangle2D);
  
  public abstract double getRotation();
  
  public abstract void setRotation(double paramDouble);
  
  public abstract void setFlipHorizontal(boolean paramBoolean);
  
  public abstract void setFlipVertical(boolean paramBoolean);
  
  public abstract boolean getFlipHorizontal();
  
  public abstract boolean getFlipVertical();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.PlaceableShape
 * JD-Core Version:    0.7.0.1
 */