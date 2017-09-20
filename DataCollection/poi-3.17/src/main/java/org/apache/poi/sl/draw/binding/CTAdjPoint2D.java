/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlType;
/*   7:    */ 
/*   8:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*   9:    */ @XmlType(name="CT_AdjPoint2D", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  10:    */ public class CTAdjPoint2D
/*  11:    */ {
/*  12:    */   @XmlAttribute(required=true)
/*  13:    */   protected String x;
/*  14:    */   @XmlAttribute(required=true)
/*  15:    */   protected String y;
/*  16:    */   
/*  17:    */   public String getX()
/*  18:    */   {
/*  19: 62 */     return this.x;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void setX(String value)
/*  23:    */   {
/*  24: 74 */     this.x = value;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public boolean isSetX()
/*  28:    */   {
/*  29: 78 */     return this.x != null;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String getY()
/*  33:    */   {
/*  34: 90 */     return this.y;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setY(String value)
/*  38:    */   {
/*  39:102 */     this.y = value;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public boolean isSetY()
/*  43:    */   {
/*  44:106 */     return this.y != null;
/*  45:    */   }
/*  46:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTAdjPoint2D
 * JD-Core Version:    0.7.0.1
 */