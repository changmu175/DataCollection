/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlEnum;
/*   4:    */ import javax.xml.bind.annotation.XmlEnumValue;
/*   5:    */ import javax.xml.bind.annotation.XmlType;
/*   6:    */ 
/*   7:    */ @XmlType(name="ST_PathFillMode", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*   8:    */ @XmlEnum
/*   9:    */ public enum STPathFillMode
/*  10:    */ {
/*  11: 53 */   NONE("none"),  NORM("norm"),  LIGHTEN("lighten"),  LIGHTEN_LESS("lightenLess"),  DARKEN("darken"),  DARKEN_LESS("darkenLess");
/*  12:    */   
/*  13:    */   private final String value;
/*  14:    */   
/*  15:    */   private STPathFillMode(String v)
/*  16:    */   {
/*  17: 93 */     this.value = v;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public String value()
/*  21:    */   {
/*  22: 97 */     return this.value;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static STPathFillMode fromValue(String v)
/*  26:    */   {
/*  27:101 */     for (STPathFillMode c : ) {
/*  28:102 */       if (c.value.equals(v)) {
/*  29:103 */         return c;
/*  30:    */       }
/*  31:    */     }
/*  32:106 */     throw new IllegalArgumentException(v);
/*  33:    */   }
/*  34:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.STPathFillMode
 * JD-Core Version:    0.7.0.1
 */