package org.apache.poi.sl.usermodel;

public abstract interface Slide<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
  extends Sheet<S, P>
{
  public abstract Notes<S, P> getNotes();
  
  public abstract void setNotes(Notes<S, P> paramNotes);
  
  public abstract boolean getFollowMasterBackground();
  
  public abstract void setFollowMasterBackground(boolean paramBoolean);
  
  public abstract boolean getFollowMasterColourScheme();
  
  public abstract void setFollowMasterColourScheme(boolean paramBoolean);
  
  public abstract boolean getFollowMasterObjects();
  
  public abstract void setFollowMasterObjects(boolean paramBoolean);
  
  public abstract int getSlideNumber();
  
  public abstract String getTitle();
  
  public abstract boolean getDisplayPlaceholder(Placeholder paramPlaceholder);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.Slide
 * JD-Core Version:    0.7.0.1
 */