package org.apache.poi.sl.draw;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public abstract interface ImageRenderer
{
  public abstract void loadImage(InputStream paramInputStream, String paramString)
    throws IOException;
  
  public abstract void loadImage(byte[] paramArrayOfByte, String paramString)
    throws IOException;
  
  public abstract Dimension getDimension();
  
  public abstract void setAlpha(double paramDouble);
  
  public abstract BufferedImage getImage();
  
  public abstract BufferedImage getImage(Dimension paramDimension);
  
  public abstract boolean drawImage(Graphics2D paramGraphics2D, Rectangle2D paramRectangle2D);
  
  public abstract boolean drawImage(Graphics2D paramGraphics2D, Rectangle2D paramRectangle2D, Insets paramInsets);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.ImageRenderer
 * JD-Core Version:    0.7.0.1
 */