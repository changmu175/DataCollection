package org.apache.poi.sl.draw.geom;

import java.awt.geom.Path2D.Double;

public abstract interface PathCommand
{
  public abstract void execute(Path2D.Double paramDouble, Context paramContext);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.PathCommand
 * JD-Core Version:    0.7.0.1
 */