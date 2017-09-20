package org.apache.poi.wp.usermodel;

public abstract interface CharacterRun
{
  public abstract boolean isBold();
  
  public abstract void setBold(boolean paramBoolean);
  
  public abstract boolean isItalic();
  
  public abstract void setItalic(boolean paramBoolean);
  
  public abstract boolean isSmallCaps();
  
  public abstract void setSmallCaps(boolean paramBoolean);
  
  public abstract boolean isCapitalized();
  
  public abstract void setCapitalized(boolean paramBoolean);
  
  public abstract boolean isStrikeThrough();
  
  public abstract void setStrikeThrough(boolean paramBoolean);
  
  public abstract boolean isDoubleStrikeThrough();
  
  public abstract void setDoubleStrikethrough(boolean paramBoolean);
  
  public abstract boolean isShadowed();
  
  public abstract void setShadow(boolean paramBoolean);
  
  public abstract boolean isEmbossed();
  
  public abstract void setEmbossed(boolean paramBoolean);
  
  public abstract boolean isImprinted();
  
  public abstract void setImprinted(boolean paramBoolean);
  
  public abstract int getFontSize();
  
  public abstract void setFontSize(int paramInt);
  
  public abstract int getCharacterSpacing();
  
  public abstract void setCharacterSpacing(int paramInt);
  
  public abstract int getKerning();
  
  public abstract void setKerning(int paramInt);
  
  public abstract boolean isHighlighted();
  
  public abstract String getFontName();
  
  public abstract String text();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.wp.usermodel.CharacterRun
 * JD-Core Version:    0.7.0.1
 */