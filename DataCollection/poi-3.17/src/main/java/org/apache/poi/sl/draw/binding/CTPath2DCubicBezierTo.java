/*  1:   */ package org.apache.poi.sl.draw.binding;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import javax.xml.bind.annotation.XmlAccessType;
/*  6:   */ import javax.xml.bind.annotation.XmlAccessorType;
/*  7:   */ import javax.xml.bind.annotation.XmlElement;
/*  8:   */ import javax.xml.bind.annotation.XmlType;
/*  9:   */ 
/* 10:   */ @XmlAccessorType(XmlAccessType.FIELD)
/* 11:   */ @XmlType(name="CT_Path2DCubicBezierTo", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"pt"})
/* 12:   */ public class CTPath2DCubicBezierTo
/* 13:   */ {
/* 14:   */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", required=true)
/* 15:   */   protected List<CTAdjPoint2D> pt;
/* 16:   */   
/* 17:   */   public List<CTAdjPoint2D> getPt()
/* 18:   */   {
/* 19:79 */     if (this.pt == null) {
/* 20:80 */       this.pt = new ArrayList();
/* 21:   */     }
/* 22:82 */     return this.pt;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean isSetPt()
/* 26:   */   {
/* 27:86 */     return (this.pt != null) && (!this.pt.isEmpty());
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void unsetPt()
/* 31:   */   {
/* 32:90 */     this.pt = null;
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTPath2DCubicBezierTo
 * JD-Core Version:    0.7.0.1
 */