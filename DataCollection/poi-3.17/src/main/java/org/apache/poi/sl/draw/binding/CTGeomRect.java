/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlType;
/*   7:    */ 
/*   8:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*   9:    */ @XmlType(name="CT_GeomRect", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  10:    */ public class CTGeomRect
/*  11:    */ {
/*  12:    */   @XmlAttribute(required=true)
/*  13:    */   protected String l;
/*  14:    */   @XmlAttribute(required=true)
/*  15:    */   protected String t;
/*  16:    */   @XmlAttribute(required=true)
/*  17:    */   protected String r;
/*  18:    */   @XmlAttribute(required=true)
/*  19:    */   protected String b;
/*  20:    */   
/*  21:    */   public String getL()
/*  22:    */   {
/*  23: 68 */     return this.l;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setL(String value)
/*  27:    */   {
/*  28: 80 */     this.l = value;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public boolean isSetL()
/*  32:    */   {
/*  33: 84 */     return this.l != null;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public String getT()
/*  37:    */   {
/*  38: 96 */     return this.t;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setT(String value)
/*  42:    */   {
/*  43:108 */     this.t = value;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean isSetT()
/*  47:    */   {
/*  48:112 */     return this.t != null;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public String getR()
/*  52:    */   {
/*  53:124 */     return this.r;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setR(String value)
/*  57:    */   {
/*  58:136 */     this.r = value;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public boolean isSetR()
/*  62:    */   {
/*  63:140 */     return this.r != null;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public String getB()
/*  67:    */   {
/*  68:152 */     return this.b;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setB(String value)
/*  72:    */   {
/*  73:164 */     this.b = value;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public boolean isSetB()
/*  77:    */   {
/*  78:168 */     return this.b != null;
/*  79:    */   }
/*  80:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTGeomRect
 * JD-Core Version:    0.7.0.1
 */