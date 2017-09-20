package org.apache.poi.sl.usermodel;

public abstract interface Hyperlink<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
  extends org.apache.poi.common.usermodel.Hyperlink
{
  public abstract void linkToEmail(String paramString);
  
  public abstract void linkToUrl(String paramString);
  
  public abstract void linkToSlide(Slide<S, P> paramSlide);
  
  public abstract void linkToNextSlide();
  
  public abstract void linkToPreviousSlide();
  
  public abstract void linkToFirstSlide();
  
  public abstract void linkToLastSlide();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.Hyperlink
 * JD-Core Version:    0.7.0.1
 */