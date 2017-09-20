package org.apache.poi.sl.usermodel;

import java.awt.Graphics2D;

public abstract interface Sheet<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
  extends ShapeContainer<S, P>
{
  public abstract SlideShow<S, P> getSlideShow();
  
  public abstract boolean getFollowMasterGraphics();
  
  public abstract MasterSheet<S, P> getMasterSheet();
  
  public abstract Background<S, P> getBackground();
  
  public abstract void draw(Graphics2D paramGraphics2D);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.Sheet
 * JD-Core Version:    0.7.0.1
 */