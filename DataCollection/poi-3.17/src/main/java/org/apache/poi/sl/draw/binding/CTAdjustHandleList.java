/*  1:   */ package org.apache.poi.sl.draw.binding;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import javax.xml.bind.annotation.XmlAccessType;
/*  6:   */ import javax.xml.bind.annotation.XmlAccessorType;
/*  7:   */ import javax.xml.bind.annotation.XmlElements;
/*  8:   */ import javax.xml.bind.annotation.XmlType;
/*  9:   */ 
/* 10:   */ @XmlAccessorType(XmlAccessType.FIELD)
/* 11:   */ @XmlType(name="CT_AdjustHandleList", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"ahXYOrAhPolar"})
/* 12:   */ public class CTAdjustHandleList
/* 13:   */ {
/* 14:   */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="ahXY", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=CTXYAdjustHandle.class), @javax.xml.bind.annotation.XmlElement(name="ahPolar", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=CTPolarAdjustHandle.class)})
/* 15:   */   protected List<Object> ahXYOrAhPolar;
/* 16:   */   
/* 17:   */   public List<Object> getAhXYOrAhPolar()
/* 18:   */   {
/* 19:85 */     if (this.ahXYOrAhPolar == null) {
/* 20:86 */       this.ahXYOrAhPolar = new ArrayList();
/* 21:   */     }
/* 22:88 */     return this.ahXYOrAhPolar;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean isSetAhXYOrAhPolar()
/* 26:   */   {
/* 27:92 */     return (this.ahXYOrAhPolar != null) && (!this.ahXYOrAhPolar.isEmpty());
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void unsetAhXYOrAhPolar()
/* 31:   */   {
/* 32:96 */     this.ahXYOrAhPolar = null;
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTAdjustHandleList
 * JD-Core Version:    0.7.0.1
 */