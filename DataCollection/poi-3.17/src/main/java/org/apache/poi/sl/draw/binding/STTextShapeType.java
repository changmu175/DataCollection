/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlEnum;
/*   4:    */ import javax.xml.bind.annotation.XmlEnumValue;
/*   5:    */ import javax.xml.bind.annotation.XmlType;
/*   6:    */ 
/*   7:    */ @XmlType(name="ST_TextShapeType", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*   8:    */ @XmlEnum
/*   9:    */ public enum STTextShapeType
/*  10:    */ {
/*  11: 88 */   TEXT_NO_SHAPE("textNoShape"),  TEXT_PLAIN("textPlain"),  TEXT_STOP("textStop"),  TEXT_TRIANGLE("textTriangle"),  TEXT_TRIANGLE_INVERTED("textTriangleInverted"),  TEXT_CHEVRON("textChevron"),  TEXT_CHEVRON_INVERTED("textChevronInverted"),  TEXT_RING_INSIDE("textRingInside"),  TEXT_RING_OUTSIDE("textRingOutside"),  TEXT_ARCH_UP("textArchUp"),  TEXT_ARCH_DOWN("textArchDown"),  TEXT_CIRCLE("textCircle"),  TEXT_BUTTON("textButton"),  TEXT_ARCH_UP_POUR("textArchUpPour"),  TEXT_ARCH_DOWN_POUR("textArchDownPour"),  TEXT_CIRCLE_POUR("textCirclePour"),  TEXT_BUTTON_POUR("textButtonPour"),  TEXT_CURVE_UP("textCurveUp"),  TEXT_CURVE_DOWN("textCurveDown"),  TEXT_CAN_UP("textCanUp"),  TEXT_CAN_DOWN("textCanDown"),  TEXT_WAVE_1("textWave1"),  TEXT_WAVE_2("textWave2"),  TEXT_DOUBLE_WAVE_1("textDoubleWave1"),  TEXT_WAVE_4("textWave4"),  TEXT_INFLATE("textInflate"),  TEXT_DEFLATE("textDeflate"),  TEXT_INFLATE_BOTTOM("textInflateBottom"),  TEXT_DEFLATE_BOTTOM("textDeflateBottom"),  TEXT_INFLATE_TOP("textInflateTop"),  TEXT_DEFLATE_TOP("textDeflateTop"),  TEXT_DEFLATE_INFLATE("textDeflateInflate"),  TEXT_DEFLATE_INFLATE_DEFLATE("textDeflateInflateDeflate"),  TEXT_FADE_RIGHT("textFadeRight"),  TEXT_FADE_LEFT("textFadeLeft"),  TEXT_FADE_UP("textFadeUp"),  TEXT_FADE_DOWN("textFadeDown"),  TEXT_SLANT_UP("textSlantUp"),  TEXT_SLANT_DOWN("textSlantDown"),  TEXT_CASCADE_UP("textCascadeUp"),  TEXT_CASCADE_DOWN("textCascadeDown");
/*  12:    */   
/*  13:    */   private final String value;
/*  14:    */   
/*  15:    */   private STTextShapeType(String v)
/*  16:    */   {
/*  17:373 */     this.value = v;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public String value()
/*  21:    */   {
/*  22:377 */     return this.value;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static STTextShapeType fromValue(String v)
/*  26:    */   {
/*  27:381 */     for (STTextShapeType c : ) {
/*  28:382 */       if (c.value.equals(v)) {
/*  29:383 */         return c;
/*  30:    */       }
/*  31:    */     }
/*  32:386 */     throw new IllegalArgumentException(v);
/*  33:    */   }
/*  34:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.STTextShapeType
 * JD-Core Version:    0.7.0.1
 */