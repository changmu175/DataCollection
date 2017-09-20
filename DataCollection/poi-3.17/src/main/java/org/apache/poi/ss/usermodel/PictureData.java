package org.apache.poi.ss.usermodel;

public abstract interface PictureData
{
  public abstract byte[] getData();
  
  public abstract String suggestFileExtension();
  
  public abstract String getMimeType();
  
  public abstract int getPictureType();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.PictureData
 * JD-Core Version:    0.7.0.1
 */