/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlElement;
/*   7:    */ import javax.xml.bind.annotation.XmlType;
/*   8:    */ 
/*   9:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*  10:    */ @XmlType(name="CT_ConnectionSite", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"pos"})
/*  11:    */ public class CTConnectionSite
/*  12:    */ {
/*  13:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", required=true)
/*  14:    */   protected CTAdjPoint2D pos;
/*  15:    */   @XmlAttribute(required=true)
/*  16:    */   protected String ang;
/*  17:    */   
/*  18:    */   public CTAdjPoint2D getPos()
/*  19:    */   {
/*  20: 67 */     return this.pos;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setPos(CTAdjPoint2D value)
/*  24:    */   {
/*  25: 79 */     this.pos = value;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public boolean isSetPos()
/*  29:    */   {
/*  30: 83 */     return this.pos != null;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String getAng()
/*  34:    */   {
/*  35: 95 */     return this.ang;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setAng(String value)
/*  39:    */   {
/*  40:107 */     this.ang = value;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public boolean isSetAng()
/*  44:    */   {
/*  45:111 */     return this.ang != null;
/*  46:    */   }
/*  47:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTConnectionSite
 * JD-Core Version:    0.7.0.1
 */