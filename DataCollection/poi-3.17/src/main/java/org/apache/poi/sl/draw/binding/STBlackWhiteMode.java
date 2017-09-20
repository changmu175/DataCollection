/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlEnum;
/*   4:    */ import javax.xml.bind.annotation.XmlEnumValue;
/*   5:    */ import javax.xml.bind.annotation.XmlType;
/*   6:    */ 
/*   7:    */ @XmlType(name="ST_BlackWhiteMode", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*   8:    */ @XmlEnum
/*   9:    */ public enum STBlackWhiteMode
/*  10:    */ {
/*  11: 58 */   CLR("clr"),  AUTO("auto"),  GRAY("gray"),  LT_GRAY("ltGray"),  INV_GRAY("invGray"),  GRAY_WHITE("grayWhite"),  BLACK_GRAY("blackGray"),  BLACK_WHITE("blackWhite"),  BLACK("black"),  WHITE("white"),  HIDDEN("hidden");
/*  12:    */   
/*  13:    */   private final String value;
/*  14:    */   
/*  15:    */   private STBlackWhiteMode(String v)
/*  16:    */   {
/*  17:133 */     this.value = v;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public String value()
/*  21:    */   {
/*  22:137 */     return this.value;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static STBlackWhiteMode fromValue(String v)
/*  26:    */   {
/*  27:141 */     for (STBlackWhiteMode c : ) {
/*  28:142 */       if (c.value.equals(v)) {
/*  29:143 */         return c;
/*  30:    */       }
/*  31:    */     }
/*  32:146 */     throw new IllegalArgumentException(v);
/*  33:    */   }
/*  34:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.STBlackWhiteMode
 * JD-Core Version:    0.7.0.1
 */