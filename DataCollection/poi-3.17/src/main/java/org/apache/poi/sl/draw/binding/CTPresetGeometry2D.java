/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlElement;
/*   7:    */ import javax.xml.bind.annotation.XmlType;
/*   8:    */ 
/*   9:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*  10:    */ @XmlType(name="CT_PresetGeometry2D", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"avLst"})
/*  11:    */ public class CTPresetGeometry2D
/*  12:    */ {
/*  13:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  14:    */   protected CTGeomGuideList avLst;
/*  15:    */   @XmlAttribute(required=true)
/*  16:    */   protected STShapeType prst;
/*  17:    */   
/*  18:    */   public CTGeomGuideList getAvLst()
/*  19:    */   {
/*  20: 67 */     return this.avLst;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setAvLst(CTGeomGuideList value)
/*  24:    */   {
/*  25: 79 */     this.avLst = value;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public boolean isSetAvLst()
/*  29:    */   {
/*  30: 83 */     return this.avLst != null;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public STShapeType getPrst()
/*  34:    */   {
/*  35: 95 */     return this.prst;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setPrst(STShapeType value)
/*  39:    */   {
/*  40:107 */     this.prst = value;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public boolean isSetPrst()
/*  44:    */   {
/*  45:111 */     return this.prst != null;
/*  46:    */   }
/*  47:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTPresetGeometry2D
 * JD-Core Version:    0.7.0.1
 */