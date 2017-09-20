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
/* 11:   */ @XmlType(name="CT_Path2DList", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"path"})
/* 12:   */ public class CTPath2DList
/* 13:   */ {
/* 14:   */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/* 15:   */   protected List<CTPath2D> path;
/* 16:   */   
/* 17:   */   public List<CTPath2D> getPath()
/* 18:   */   {
/* 19:79 */     if (this.path == null) {
/* 20:80 */       this.path = new ArrayList();
/* 21:   */     }
/* 22:82 */     return this.path;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean isSetPath()
/* 26:   */   {
/* 27:86 */     return (this.path != null) && (!this.path.isEmpty());
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void unsetPath()
/* 31:   */   {
/* 32:90 */     this.path = null;
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTPath2DList
 * JD-Core Version:    0.7.0.1
 */