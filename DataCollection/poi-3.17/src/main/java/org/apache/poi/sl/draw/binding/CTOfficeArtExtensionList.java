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
/* 11:   */ @XmlType(name="CT_OfficeArtExtensionList", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"ext"})
/* 12:   */ public class CTOfficeArtExtensionList
/* 13:   */ {
/* 14:   */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/* 15:   */   protected List<CTOfficeArtExtension> ext;
/* 16:   */   
/* 17:   */   public List<CTOfficeArtExtension> getExt()
/* 18:   */   {
/* 19:79 */     if (this.ext == null) {
/* 20:80 */       this.ext = new ArrayList();
/* 21:   */     }
/* 22:82 */     return this.ext;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean isSetExt()
/* 26:   */   {
/* 27:86 */     return (this.ext != null) && (!this.ext.isEmpty());
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void unsetExt()
/* 31:   */   {
/* 32:90 */     this.ext = null;
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTOfficeArtExtensionList
 * JD-Core Version:    0.7.0.1
 */