/*  1:   */ package org.apache.poi.xdgf.usermodel;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ import org.apache.poi.POIXMLRelation;
/*  6:   */ import org.apache.poi.xdgf.xml.XDGFXMLDocumentPart;
/*  7:   */ 
/*  8:   */ public class XDGFRelation
/*  9:   */   extends POIXMLRelation
/* 10:   */ {
/* 11:32 */   private static final Map<String, XDGFRelation> _table = new HashMap();
/* 12:34 */   public static final XDGFRelation DOCUMENT = new XDGFRelation("application/vnd.ms-visio.drawing.main+xml", "http://schemas.microsoft.com/visio/2010/relationships/document", "/visio/document.xml", null);
/* 13:39 */   public static final XDGFRelation MASTERS = new XDGFRelation("application/vnd.ms-visio.masters+xml", "http://schemas.microsoft.com/visio/2010/relationships/masters", "/visio/masters/masters.xml", XDGFMasters.class);
/* 14:44 */   public static final XDGFRelation MASTER = new XDGFRelation("application/vnd.ms-visio.master+xml", "http://schemas.microsoft.com/visio/2010/relationships/master", "/visio/masters/master#.xml", XDGFMasterContents.class);
/* 15:49 */   public static final XDGFRelation IMAGES = new XDGFRelation(null, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", null, null);
/* 16:53 */   public static final XDGFRelation PAGES = new XDGFRelation("application/vnd.ms-visio.pages+xml", "http://schemas.microsoft.com/visio/2010/relationships/pages", "/visio/pages/pages.xml", XDGFPages.class);
/* 17:58 */   public static final XDGFRelation PAGE = new XDGFRelation("application/vnd.ms-visio.page+xml", "http://schemas.microsoft.com/visio/2010/relationships/page", "/visio/pages/page#.xml", XDGFPageContents.class);
/* 18:63 */   public static final XDGFRelation WINDOW = new XDGFRelation("application/vnd.ms-visio.windows+xml", "http://schemas.microsoft.com/visio/2010/relationships/windows", "/visio/windows.xml", null);
/* 19:   */   
/* 20:   */   private XDGFRelation(String type, String rel, String defaultName, Class<? extends XDGFXMLDocumentPart> cls)
/* 21:   */   {
/* 22:69 */     super(type, rel, defaultName, cls);
/* 23:70 */     _table.put(rel, this);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public static XDGFRelation getInstance(String rel)
/* 27:   */   {
/* 28:82 */     return (XDGFRelation)_table.get(rel);
/* 29:   */   }
/* 30:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFRelation
 * JD-Core Version:    0.7.0.1
 */