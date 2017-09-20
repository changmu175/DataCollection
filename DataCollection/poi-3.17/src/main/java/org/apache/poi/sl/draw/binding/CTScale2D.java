/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlElement;
/*   6:    */ import javax.xml.bind.annotation.XmlType;
/*   7:    */ 
/*   8:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*   9:    */ @XmlType(name="CT_Scale2D", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"sx", "sy"})
/*  10:    */ public class CTScale2D
/*  11:    */ {
/*  12:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", required=true)
/*  13:    */   protected CTRatio sx;
/*  14:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", required=true)
/*  15:    */   protected CTRatio sy;
/*  16:    */   
/*  17:    */   public CTRatio getSx()
/*  18:    */   {
/*  19: 67 */     return this.sx;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void setSx(CTRatio value)
/*  23:    */   {
/*  24: 79 */     this.sx = value;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public boolean isSetSx()
/*  28:    */   {
/*  29: 83 */     return this.sx != null;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public CTRatio getSy()
/*  33:    */   {
/*  34: 95 */     return this.sy;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setSy(CTRatio value)
/*  38:    */   {
/*  39:107 */     this.sy = value;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public boolean isSetSy()
/*  43:    */   {
/*  44:111 */     return this.sy != null;
/*  45:    */   }
/*  46:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTScale2D
 * JD-Core Version:    0.7.0.1
 */