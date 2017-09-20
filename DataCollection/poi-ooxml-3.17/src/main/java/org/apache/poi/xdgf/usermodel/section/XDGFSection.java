/*  1:   */ package org.apache.poi.xdgf.usermodel.section;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*  4:   */ import com.microsoft.schemas.office.visio.x2012.main.SectionType;
/*  5:   */ import java.util.HashMap;
/*  6:   */ import java.util.Map;
/*  7:   */ import org.apache.poi.POIXMLException;
/*  8:   */ import org.apache.poi.util.Internal;
/*  9:   */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/* 10:   */ import org.apache.poi.xdgf.usermodel.XDGFSheet;
/* 11:   */ import org.apache.poi.xdgf.util.ObjectFactory;
/* 12:   */ 
/* 13:   */ public abstract class XDGFSection
/* 14:   */ {
/* 15:37 */   static final ObjectFactory<XDGFSection, SectionType> _sectionTypes = new ObjectFactory();
/* 16:   */   protected SectionType _section;
/* 17:   */   protected XDGFSheet _containingSheet;
/* 18:   */   
/* 19:   */   static
/* 20:   */   {
/* 21:   */     try
/* 22:   */     {
/* 23:39 */       _sectionTypes.put("LineGradient", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 24:40 */       _sectionTypes.put("FillGradient", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 25:41 */       _sectionTypes.put("Character", CharacterSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 26:42 */       _sectionTypes.put("Paragraph", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 27:43 */       _sectionTypes.put("Tabs", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 28:44 */       _sectionTypes.put("Scratch", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 29:45 */       _sectionTypes.put("Connection", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 30:46 */       _sectionTypes.put("ConnectionABCD", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 31:47 */       _sectionTypes.put("Field", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 32:48 */       _sectionTypes.put("Control", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 33:49 */       _sectionTypes.put("Geometry", GeometrySection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 34:50 */       _sectionTypes.put("Actions", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 35:51 */       _sectionTypes.put("Layer", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 36:52 */       _sectionTypes.put("User", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 37:53 */       _sectionTypes.put("Property", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 38:54 */       _sectionTypes.put("Hyperlink", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 39:55 */       _sectionTypes.put("Reviewer", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 40:56 */       _sectionTypes.put("Annotation", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 41:57 */       _sectionTypes.put("ActionTag", GenericSection.class, new Class[] { SectionType.class, XDGFSheet.class });
/* 42:   */     }
/* 43:   */     catch (NoSuchMethodException e)
/* 44:   */     {
/* 45:59 */       throw new POIXMLException("Internal error");
/* 46:   */     }
/* 47:   */     catch (SecurityException e)
/* 48:   */     {
/* 49:61 */       throw new POIXMLException("Internal error");
/* 50:   */     }
/* 51:   */   }
/* 52:   */   
/* 53:   */   public static XDGFSection load(SectionType section, XDGFSheet containingSheet)
/* 54:   */   {
/* 55:67 */     return (XDGFSection)_sectionTypes.load(section.getN(), new Object[] { section, containingSheet });
/* 56:   */   }
/* 57:   */   
/* 58:74 */   protected Map<String, XDGFCell> _cells = new HashMap();
/* 59:   */   
/* 60:   */   public XDGFSection(SectionType section, XDGFSheet containingSheet)
/* 61:   */   {
/* 62:78 */     this._section = section;
/* 63:79 */     this._containingSheet = containingSheet;
/* 64:83 */     for (CellType cell : section.getCellArray()) {
/* 65:84 */       this._cells.put(cell.getN(), new XDGFCell(cell));
/* 66:   */     }
/* 67:   */   }
/* 68:   */   
/* 69:   */   @Internal
/* 70:   */   public SectionType getXmlObject()
/* 71:   */   {
/* 72:90 */     return this._section;
/* 73:   */   }
/* 74:   */   
/* 75:   */   public String toString()
/* 76:   */   {
/* 77:95 */     return "<Section type=" + this._section.getN() + " from " + this._containingSheet + ">";
/* 78:   */   }
/* 79:   */   
/* 80:   */   public abstract void setupMaster(XDGFSection paramXDGFSection);
/* 81:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.XDGFSection
 * JD-Core Version:    0.7.0.1
 */