package org.apache.poi.sl.usermodel;

public abstract interface Resources
{
  public abstract FontCollection getFontCollection();
  
  public abstract PictureData[] getPictureData();
  
  public abstract int addPictureData(PictureData paramPictureData);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.Resources
 * JD-Core Version:    0.7.0.1
 */