/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlType;
/*   7:    */ 
/*   8:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*   9:    */ @XmlType(name="CT_SphereCoords", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  10:    */ public class CTSphereCoords
/*  11:    */ {
/*  12:    */   @XmlAttribute(required=true)
/*  13:    */   protected int lat;
/*  14:    */   @XmlAttribute(required=true)
/*  15:    */   protected int lon;
/*  16:    */   @XmlAttribute(required=true)
/*  17:    */   protected int rev;
/*  18:    */   
/*  19:    */   public int getLat()
/*  20:    */   {
/*  21: 61 */     return this.lat;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setLat(int value)
/*  25:    */   {
/*  26: 69 */     this.lat = value;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public boolean isSetLat()
/*  30:    */   {
/*  31: 73 */     return true;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public int getLon()
/*  35:    */   {
/*  36: 81 */     return this.lon;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setLon(int value)
/*  40:    */   {
/*  41: 89 */     this.lon = value;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public boolean isSetLon()
/*  45:    */   {
/*  46: 93 */     return true;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public int getRev()
/*  50:    */   {
/*  51:101 */     return this.rev;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setRev(int value)
/*  55:    */   {
/*  56:109 */     this.rev = value;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean isSetRev()
/*  60:    */   {
/*  61:113 */     return true;
/*  62:    */   }
/*  63:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTSphereCoords
 * JD-Core Version:    0.7.0.1
 */