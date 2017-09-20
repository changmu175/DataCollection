/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlType;
/*   7:    */ import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
/*   8:    */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*   9:    */ 
/*  10:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*  11:    */ @XmlType(name="CT_GeomGuide", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  12:    */ public class CTGeomGuide
/*  13:    */ {
/*  14:    */   @XmlAttribute(required=true)
/*  15:    */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*  16:    */   protected String name;
/*  17:    */   @XmlAttribute(required=true)
/*  18:    */   protected String fmla;
/*  19:    */   
/*  20:    */   public String getName()
/*  21:    */   {
/*  22: 65 */     return this.name;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setName(String value)
/*  26:    */   {
/*  27: 77 */     this.name = value;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public boolean isSetName()
/*  31:    */   {
/*  32: 81 */     return this.name != null;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public String getFmla()
/*  36:    */   {
/*  37: 93 */     return this.fmla;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setFmla(String value)
/*  41:    */   {
/*  42:105 */     this.fmla = value;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public boolean isSetFmla()
/*  46:    */   {
/*  47:109 */     return this.fmla != null;
/*  48:    */   }
/*  49:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTGeomGuide
 * JD-Core Version:    0.7.0.1
 */