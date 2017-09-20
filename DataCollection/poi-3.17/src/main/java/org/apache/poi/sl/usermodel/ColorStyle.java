package org.apache.poi.sl.usermodel;

import java.awt.Color;

public abstract interface ColorStyle
{
  public abstract Color getColor();
  
  public abstract int getAlpha();
  
  public abstract int getHueOff();
  
  public abstract int getHueMod();
  
  public abstract int getSatOff();
  
  public abstract int getSatMod();
  
  public abstract int getLumOff();
  
  public abstract int getLumMod();
  
  public abstract int getShade();
  
  public abstract int getTint();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.ColorStyle
 * JD-Core Version:    0.7.0.1
 */