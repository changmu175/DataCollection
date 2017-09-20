/*  1:   */ package org.apache.poi.sl.draw.binding;
/*  2:   */ 
/*  3:   */ import javax.xml.bind.annotation.XmlAccessType;
/*  4:   */ import javax.xml.bind.annotation.XmlAccessorType;
/*  5:   */ import javax.xml.bind.annotation.XmlElement;
/*  6:   */ import javax.xml.bind.annotation.XmlType;
/*  7:   */ 
/*  8:   */ @XmlAccessorType(XmlAccessType.FIELD)
/*  9:   */ @XmlType(name="CT_Path2DLineTo", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"pt"})
/* 10:   */ public class CTPath2DLineTo
/* 11:   */ {
/* 12:   */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", required=true)
/* 13:   */   protected CTAdjPoint2D pt;
/* 14:   */   
/* 15:   */   public CTAdjPoint2D getPt()
/* 16:   */   {
/* 17:63 */     return this.pt;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void setPt(CTAdjPoint2D value)
/* 21:   */   {
/* 22:75 */     this.pt = value;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean isSetPt()
/* 26:   */   {
/* 27:79 */     return this.pt != null;
/* 28:   */   }
/* 29:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTPath2DLineTo
 * JD-Core Version:    0.7.0.1
 */