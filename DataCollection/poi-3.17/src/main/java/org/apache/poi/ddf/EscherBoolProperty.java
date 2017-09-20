/*  1:   */ package org.apache.poi.ddf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.HexDump;
/*  4:   */ 
/*  5:   */ public class EscherBoolProperty
/*  6:   */   extends EscherSimpleProperty
/*  7:   */ {
/*  8:   */   public EscherBoolProperty(short propertyNumber, int value)
/*  9:   */   {
/* 10:42 */     super(propertyNumber, value);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public boolean isTrue()
/* 14:   */   {
/* 15:52 */     return getPropertyValue() != 0;
/* 16:   */   }
/* 17:   */   
/* 18:   */   @Deprecated
/* 19:   */   public boolean isFalse()
/* 20:   */   {
/* 21:65 */     return !isTrue();
/* 22:   */   }
/* 23:   */   
/* 24:   */   public String toXml(String tab)
/* 25:   */   {
/* 26:78 */     StringBuilder builder = new StringBuilder();
/* 27:79 */     builder.append(tab).append("<").append(getClass().getSimpleName()).append(" id=\"0x").append(HexDump.toHex(getId())).append("\" name=\"").append(getName()).append("\" simpleValue=\"").append(getPropertyValue()).append("\" blipId=\"").append(isBlipId()).append("\" value=\"").append(isTrue()).append("\"").append("/>");
/* 28:   */     
/* 29:   */ 
/* 30:82 */     return builder.toString();
/* 31:   */   }
/* 32:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherBoolProperty
 * JD-Core Version:    0.7.0.1
 */