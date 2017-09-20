/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlEnum;
/*   4:    */ import javax.xml.bind.annotation.XmlEnumValue;
/*   5:    */ import javax.xml.bind.annotation.XmlType;
/*   6:    */ 
/*   7:    */ @XmlType(name="ST_RectAlignment", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*   8:    */ @XmlEnum
/*   9:    */ public enum STRectAlignment
/*  10:    */ {
/*  11: 56 */   TL("tl"),  T("t"),  TR("tr"),  L("l"),  CTR("ctr"),  R("r"),  BL("bl"),  B("b"),  BR("br");
/*  12:    */   
/*  13:    */   private final String value;
/*  14:    */   
/*  15:    */   private STRectAlignment(String v)
/*  16:    */   {
/*  17:117 */     this.value = v;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public String value()
/*  21:    */   {
/*  22:121 */     return this.value;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static STRectAlignment fromValue(String v)
/*  26:    */   {
/*  27:125 */     for (STRectAlignment c : ) {
/*  28:126 */       if (c.value.equals(v)) {
/*  29:127 */         return c;
/*  30:    */       }
/*  31:    */     }
/*  32:130 */     throw new IllegalArgumentException(v);
/*  33:    */   }
/*  34:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.STRectAlignment
 * JD-Core Version:    0.7.0.1
 */