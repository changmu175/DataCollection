/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlType;
/*   7:    */ 
/*   8:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*   9:    */ @XmlType(name="CT_Point3D", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  10:    */ public class CTPoint3D
/*  11:    */ {
/*  12:    */   @XmlAttribute(required=true)
/*  13:    */   protected long x;
/*  14:    */   @XmlAttribute(required=true)
/*  15:    */   protected long y;
/*  16:    */   @XmlAttribute(required=true)
/*  17:    */   protected long z;
/*  18:    */   
/*  19:    */   public long getX()
/*  20:    */   {
/*  21: 61 */     return this.x;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setX(long value)
/*  25:    */   {
/*  26: 69 */     this.x = value;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public boolean isSetX()
/*  30:    */   {
/*  31: 73 */     return true;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public long getY()
/*  35:    */   {
/*  36: 81 */     return this.y;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setY(long value)
/*  40:    */   {
/*  41: 89 */     this.y = value;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public boolean isSetY()
/*  45:    */   {
/*  46: 93 */     return true;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public long getZ()
/*  50:    */   {
/*  51:101 */     return this.z;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setZ(long value)
/*  55:    */   {
/*  56:109 */     this.z = value;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean isSetZ()
/*  60:    */   {
/*  61:113 */     return true;
/*  62:    */   }
/*  63:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTPoint3D
 * JD-Core Version:    0.7.0.1
 */