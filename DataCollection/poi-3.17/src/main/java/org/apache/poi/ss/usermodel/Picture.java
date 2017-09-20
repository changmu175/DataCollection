package org.apache.poi.ss.usermodel;

import java.awt.Dimension;

public abstract interface Picture
  extends Shape
{
  public abstract void resize();
  
  public abstract void resize(double paramDouble);
  
  public abstract void resize(double paramDouble1, double paramDouble2);
  
  public abstract ClientAnchor getPreferredSize();
  
  public abstract ClientAnchor getPreferredSize(double paramDouble1, double paramDouble2);
  
  public abstract Dimension getImageDimension();
  
  public abstract PictureData getPictureData();
  
  public abstract ClientAnchor getClientAnchor();
  
  public abstract Sheet getSheet();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Picture
 * JD-Core Version:    0.7.0.1
 */