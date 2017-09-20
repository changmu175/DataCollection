package org.apache.poi.sl.usermodel;

import java.awt.geom.Path2D.Double;

public abstract interface FreeformShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
  extends AutoShape<S, P>
{
  public abstract Path2D.Double getPath();
  
  public abstract int setPath(Path2D.Double paramDouble);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.FreeformShape
 * JD-Core Version:    0.7.0.1
 */