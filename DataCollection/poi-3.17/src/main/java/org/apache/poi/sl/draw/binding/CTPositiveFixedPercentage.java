/*  1:   */ package org.apache.poi.sl.draw.binding;
/*  2:   */ 
/*  3:   */ import javax.xml.bind.annotation.XmlAccessType;
/*  4:   */ import javax.xml.bind.annotation.XmlAccessorType;
/*  5:   */ import javax.xml.bind.annotation.XmlAttribute;
/*  6:   */ import javax.xml.bind.annotation.XmlType;
/*  7:   */ 
/*  8:   */ @XmlAccessorType(XmlAccessType.FIELD)
/*  9:   */ @XmlType(name="CT_PositiveFixedPercentage", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/* 10:   */ public class CTPositiveFixedPercentage
/* 11:   */ {
/* 12:   */   @XmlAttribute(required=true)
/* 13:   */   protected int val;
/* 14:   */   
/* 15:   */   public int getVal()
/* 16:   */   {
/* 17:55 */     return this.val;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void setVal(int value)
/* 21:   */   {
/* 22:63 */     this.val = value;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean isSetVal()
/* 26:   */   {
/* 27:67 */     return true;
/* 28:   */   }
/* 29:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTPositiveFixedPercentage
 * JD-Core Version:    0.7.0.1
 */