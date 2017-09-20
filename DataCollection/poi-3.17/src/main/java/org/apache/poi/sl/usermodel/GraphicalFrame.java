package org.apache.poi.sl.usermodel;

public abstract interface GraphicalFrame<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
  extends Shape<S, P>, PlaceableShape<S, P>
{
  public abstract PictureShape<S, P> getFallbackPicture();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.GraphicalFrame
 * JD-Core Version:    0.7.0.1
 */