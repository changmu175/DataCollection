/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlType;
/*   7:    */ 
/*   8:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*   9:    */ @XmlType(name="CT_Path2DArcTo", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  10:    */ public class CTPath2DArcTo
/*  11:    */ {
/*  12:    */   @XmlAttribute(name="wR", required=true)
/*  13:    */   protected String wr;
/*  14:    */   @XmlAttribute(name="hR", required=true)
/*  15:    */   protected String hr;
/*  16:    */   @XmlAttribute(required=true)
/*  17:    */   protected String stAng;
/*  18:    */   @XmlAttribute(required=true)
/*  19:    */   protected String swAng;
/*  20:    */   
/*  21:    */   public String getWR()
/*  22:    */   {
/*  23: 68 */     return this.wr;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setWR(String value)
/*  27:    */   {
/*  28: 80 */     this.wr = value;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public boolean isSetWR()
/*  32:    */   {
/*  33: 84 */     return this.wr != null;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public String getHR()
/*  37:    */   {
/*  38: 96 */     return this.hr;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setHR(String value)
/*  42:    */   {
/*  43:108 */     this.hr = value;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean isSetHR()
/*  47:    */   {
/*  48:112 */     return this.hr != null;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public String getStAng()
/*  52:    */   {
/*  53:124 */     return this.stAng;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setStAng(String value)
/*  57:    */   {
/*  58:136 */     this.stAng = value;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public boolean isSetStAng()
/*  62:    */   {
/*  63:140 */     return this.stAng != null;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public String getSwAng()
/*  67:    */   {
/*  68:152 */     return this.swAng;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setSwAng(String value)
/*  72:    */   {
/*  73:164 */     this.swAng = value;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public boolean isSetSwAng()
/*  77:    */   {
/*  78:168 */     return this.swAng != null;
/*  79:    */   }
/*  80:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTPath2DArcTo
 * JD-Core Version:    0.7.0.1
 */