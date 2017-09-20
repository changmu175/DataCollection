package org.apache.poi.xdgf.usermodel.section.geometry;

import java.awt.geom.Path2D.Double;
import org.apache.poi.xdgf.usermodel.XDGFShape;

public abstract interface GeometryRow
{
  public abstract void setupMaster(GeometryRow paramGeometryRow);
  
  public abstract void addToPath(Path2D.Double paramDouble, XDGFShape paramXDGFShape);
}


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.GeometryRow
 * JD-Core Version:    0.7.0.1
 */