package org.apache.poi.sl.usermodel;

import java.util.List;

public abstract interface Notes<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
  extends Sheet<S, P>
{
  public abstract List<? extends List<P>> getTextParagraphs();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.Notes
 * JD-Core Version:    0.7.0.1
 */