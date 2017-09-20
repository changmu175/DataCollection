package org.apache.poi.sl.usermodel;

import java.awt.geom.Rectangle2D;

public abstract interface GroupShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
  extends Shape<S, P>, ShapeContainer<S, P>, PlaceableShape<S, P>
{
  public abstract Rectangle2D getInteriorAnchor();
  
  public abstract void setInteriorAnchor(Rectangle2D paramRectangle2D);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.GroupShape
 * JD-Core Version:    0.7.0.1
 */