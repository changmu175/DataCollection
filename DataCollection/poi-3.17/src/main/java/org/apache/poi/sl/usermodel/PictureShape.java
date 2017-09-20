package org.apache.poi.sl.usermodel;

import java.awt.Insets;

public abstract interface PictureShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
  extends SimpleShape<S, P>
{
  public abstract PictureData getPictureData();
  
  public abstract Insets getClipping();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.PictureShape
 * JD-Core Version:    0.7.0.1
 */