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
/* 11:   */ @XmlType(name="CT_GeomGuideList", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"gd"})
/* 12:   */ public class CTGeomGuideList
/* 13:   */ {
/* 14:   */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/* 15:   */   protected List<CTGeomGuide> gd;
/* 16:   */   
/* 17:   */   public List<CTGeomGuide> getGd()
/* 18:   */   {
/* 19:79 */     if (this.gd == null) {
/* 20:80 */       this.gd = new ArrayList();
/* 21:   */     }
/* 22:82 */     return this.gd;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean isSetGd()
/* 26:   */   {
/* 27:86 */     return (this.gd != null) && (!this.gd.isEmpty());
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void unsetGd()
/* 31:   */   {
/* 32:90 */     this.gd = null;
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTGeomGuideList
 * JD-Core Version:    0.7.0.1
 */