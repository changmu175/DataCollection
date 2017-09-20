/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlType;
/*   7:    */ 
/*   8:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*   9:    */ @XmlType(name="CT_Vector3D", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  10:    */ public class CTVector3D
/*  11:    */ {
/*  12:    */   @XmlAttribute(required=true)
/*  13:    */   protected long dx;
/*  14:    */   @XmlAttribute(required=true)
/*  15:    */   protected long dy;
/*  16:    */   @XmlAttribute(required=true)
/*  17:    */   protected long dz;
/*  18:    */   
/*  19:    */   public long getDx()
/*  20:    */   {
/*  21: 61 */     return this.dx;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setDx(long value)
/*  25:    */   {
/*  26: 69 */     this.dx = value;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public boolean isSetDx()
/*  30:    */   {
/*  31: 73 */     return true;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public long getDy()
/*  35:    */   {
/*  36: 81 */     return this.dy;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setDy(long value)
/*  40:    */   {
/*  41: 89 */     this.dy = value;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public boolean isSetDy()
/*  45:    */   {
/*  46: 93 */     return true;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public long getDz()
/*  50:    */   {
/*  51:101 */     return this.dz;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setDz(long value)
/*  55:    */   {
/*  56:109 */     this.dz = value;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean isSetDz()
/*  60:    */   {
/*  61:113 */     return true;
/*  62:    */   }
/*  63:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTVector3D
 * JD-Core Version:    0.7.0.1
 */