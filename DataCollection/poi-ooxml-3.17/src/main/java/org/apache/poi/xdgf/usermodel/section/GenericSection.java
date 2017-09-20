/*  1:   */ package org.apache.poi.xdgf.usermodel.section;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.SectionType;
/*  4:   */ import org.apache.poi.xdgf.usermodel.XDGFSheet;
/*  5:   */ 
/*  6:   */ public class GenericSection
/*  7:   */   extends XDGFSection
/*  8:   */ {
/*  9:   */   public GenericSection(SectionType section, XDGFSheet containingSheet)
/* 10:   */   {
/* 11:27 */     super(section, containingSheet);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public void setupMaster(XDGFSection section) {}
/* 15:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.GenericSection
 * JD-Core Version:    0.7.0.1
 */