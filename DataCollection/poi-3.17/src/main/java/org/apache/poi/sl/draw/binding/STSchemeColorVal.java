/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlEnum;
/*   4:    */ import javax.xml.bind.annotation.XmlEnumValue;
/*   5:    */ import javax.xml.bind.annotation.XmlType;
/*   6:    */ 
/*   7:    */ @XmlType(name="ST_SchemeColorVal", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*   8:    */ @XmlEnum
/*   9:    */ public enum STSchemeColorVal
/*  10:    */ {
/*  11: 64 */   BG_1("bg1"),  TX_1("tx1"),  BG_2("bg2"),  TX_2("tx2"),  ACCENT_1("accent1"),  ACCENT_2("accent2"),  ACCENT_3("accent3"),  ACCENT_4("accent4"),  ACCENT_5("accent5"),  ACCENT_6("accent6"),  HLINK("hlink"),  FOL_HLINK("folHlink"),  PH_CLR("phClr"),  DK_1("dk1"),  LT_1("lt1"),  DK_2("dk2"),  LT_2("lt2");
/*  12:    */   
/*  13:    */   private final String value;
/*  14:    */   
/*  15:    */   private STSchemeColorVal(String v)
/*  16:    */   {
/*  17:181 */     this.value = v;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public String value()
/*  21:    */   {
/*  22:185 */     return this.value;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static STSchemeColorVal fromValue(String v)
/*  26:    */   {
/*  27:189 */     for (STSchemeColorVal c : ) {
/*  28:190 */       if (c.value.equals(v)) {
/*  29:191 */         return c;
/*  30:    */       }
/*  31:    */     }
/*  32:194 */     throw new IllegalArgumentException(v);
/*  33:    */   }
/*  34:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.STSchemeColorVal
 * JD-Core Version:    0.7.0.1
 */