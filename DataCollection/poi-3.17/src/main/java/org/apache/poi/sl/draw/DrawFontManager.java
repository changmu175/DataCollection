package org.apache.poi.sl.draw;

import java.awt.Font;
import java.awt.Graphics2D;
import org.apache.poi.common.usermodel.fonts.FontInfo;

public abstract interface DrawFontManager
{
  public abstract FontInfo getMappedFont(Graphics2D paramGraphics2D, FontInfo paramFontInfo);
  
  public abstract FontInfo getFallbackFont(Graphics2D paramGraphics2D, FontInfo paramFontInfo);
  
  public abstract String mapFontCharset(Graphics2D paramGraphics2D, FontInfo paramFontInfo, String paramString);
  
  public abstract Font createAWTFont(Graphics2D paramGraphics2D, FontInfo paramFontInfo, double paramDouble, boolean paramBoolean1, boolean paramBoolean2);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawFontManager
 * JD-Core Version:    0.7.0.1
 */