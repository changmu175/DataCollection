/*  1:   */ package org.apache.poi.sl.draw.binding;
/*  2:   */ 
/*  3:   */ import javax.xml.bind.annotation.XmlAccessType;
/*  4:   */ import javax.xml.bind.annotation.XmlAccessorType;
/*  5:   */ import javax.xml.bind.annotation.XmlAttribute;
/*  6:   */ import javax.xml.bind.annotation.XmlType;
/*  7:   */ 
/*  8:   */ @XmlAccessorType(XmlAccessType.FIELD)
/*  9:   */ @XmlType(name="CT_Ratio", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/* 10:   */ public class CTRatio
/* 11:   */ {
/* 12:   */   @XmlAttribute(required=true)
/* 13:   */   protected long n;
/* 14:   */   @XmlAttribute(required=true)
/* 15:   */   protected long d;
/* 16:   */   
/* 17:   */   public long getN()
/* 18:   */   {
/* 19:58 */     return this.n;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setN(long value)
/* 23:   */   {
/* 24:66 */     this.n = value;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public boolean isSetN()
/* 28:   */   {
/* 29:70 */     return true;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public long getD()
/* 33:   */   {
/* 34:78 */     return this.d;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void setD(long value)
/* 38:   */   {
/* 39:86 */     this.d = value;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public boolean isSetD()
/* 43:   */   {
/* 44:90 */     return true;
/* 45:   */   }
/* 46:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTRatio
 * JD-Core Version:    0.7.0.1
 */