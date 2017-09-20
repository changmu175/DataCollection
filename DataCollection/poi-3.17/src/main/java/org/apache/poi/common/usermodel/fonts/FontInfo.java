package org.apache.poi.common.usermodel.fonts;

public abstract interface FontInfo
{
  public abstract Integer getIndex();
  
  public abstract void setIndex(int paramInt);
  
  public abstract String getTypeface();
  
  public abstract void setTypeface(String paramString);
  
  public abstract FontCharset getCharset();
  
  public abstract void setCharset(FontCharset paramFontCharset);
  
  public abstract FontFamily getFamily();
  
  public abstract void setFamily(FontFamily paramFontFamily);
  
  public abstract FontPitch getPitch();
  
  public abstract void setPitch(FontPitch paramFontPitch);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.common.usermodel.fonts.FontInfo
 * JD-Core Version:    0.7.0.1
 */