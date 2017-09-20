package org.apache.poi.sl.usermodel;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract interface Shape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
{
  public abstract ShapeContainer<S, P> getParent();
  
  public abstract Sheet<S, P> getSheet();
  
  public abstract Rectangle2D getAnchor();
  
  public abstract void draw(Graphics2D paramGraphics2D, Rectangle2D paramRectangle2D);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.Shape
 * JD-Core Version:    0.7.0.1
 */