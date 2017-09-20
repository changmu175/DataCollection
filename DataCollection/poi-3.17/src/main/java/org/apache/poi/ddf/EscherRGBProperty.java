/*  1:   */ package org.apache.poi.ddf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.HexDump;
/*  4:   */ 
/*  5:   */ public class EscherRGBProperty
/*  6:   */   extends EscherSimpleProperty
/*  7:   */ {
/*  8:   */   public EscherRGBProperty(short propertyNumber, int rgbColor)
/*  9:   */   {
/* 10:31 */     super(propertyNumber, rgbColor);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public int getRgbColor()
/* 14:   */   {
/* 15:39 */     return getPropertyValue();
/* 16:   */   }
/* 17:   */   
/* 18:   */   public byte getRed()
/* 19:   */   {
/* 20:47 */     return (byte)(getRgbColor() & 0xFF);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public byte getGreen()
/* 24:   */   {
/* 25:55 */     return (byte)(getRgbColor() >> 8 & 0xFF);
/* 26:   */   }
/* 27:   */   
/* 28:   */   public byte getBlue()
/* 29:   */   {
/* 30:63 */     return (byte)(getRgbColor() >> 16 & 0xFF);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public String toXml(String tab)
/* 34:   */   {
/* 35:68 */     StringBuilder builder = new StringBuilder();
/* 36:69 */     builder.append(tab).append("<").append(getClass().getSimpleName()).append(" id=\"0x").append(HexDump.toHex(getId())).append("\" name=\"").append(getName()).append("\" blipId=\"").append(isBlipId()).append("\" value=\"0x").append(HexDump.toHex(getRgbColor())).append("\"/>");
/* 37:   */     
/* 38:   */ 
/* 39:72 */     return builder.toString();
/* 40:   */   }
/* 41:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherRGBProperty
 * JD-Core Version:    0.7.0.1
 */