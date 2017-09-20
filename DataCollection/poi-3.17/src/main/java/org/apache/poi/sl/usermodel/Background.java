package org.apache.poi.sl.usermodel;

public abstract interface Background<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
  extends Shape<S, P>
{
  public abstract FillStyle getFillStyle();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.Background
 * JD-Core Version:    0.7.0.1
 */