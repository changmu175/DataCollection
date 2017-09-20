package org.apache.poi.sl.usermodel;

import java.awt.Color;
import org.apache.poi.sl.draw.geom.CustomGeometry;
import org.apache.poi.sl.draw.geom.IAdjustableShape;

public abstract interface SimpleShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
  extends Shape<S, P>, IAdjustableShape, PlaceableShape<S, P>
{
  public abstract FillStyle getFillStyle();
  
  public abstract LineDecoration getLineDecoration();
  
  public abstract StrokeStyle getStrokeStyle();
  
  public abstract void setStrokeStyle(Object... paramVarArgs);
  
  public abstract CustomGeometry getGeometry();
  
  public abstract ShapeType getShapeType();
  
  public abstract void setShapeType(ShapeType paramShapeType);
  
  public abstract Placeholder getPlaceholder();
  
  public abstract void setPlaceholder(Placeholder paramPlaceholder);
  
  public abstract Shadow<S, P> getShadow();
  
  public abstract Color getFillColor();
  
  public abstract void setFillColor(Color paramColor);
  
  public abstract Hyperlink<S, P> getHyperlink();
  
  public abstract Hyperlink<S, P> createHyperlink();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.SimpleShape
 * JD-Core Version:    0.7.0.1
 */