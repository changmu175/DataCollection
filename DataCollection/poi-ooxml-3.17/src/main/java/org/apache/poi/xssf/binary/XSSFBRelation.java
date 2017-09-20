/*  1:   */ package org.apache.poi.xssf.binary;
/*  2:   */ 
/*  3:   */ import org.apache.poi.POIXMLDocumentPart;
/*  4:   */ import org.apache.poi.POIXMLRelation;
/*  5:   */ import org.apache.poi.util.Internal;
/*  6:   */ import org.apache.poi.util.POILogFactory;
/*  7:   */ import org.apache.poi.util.POILogger;
/*  8:   */ 
/*  9:   */ @Internal
/* 10:   */ public class XSSFBRelation
/* 11:   */   extends POIXMLRelation
/* 12:   */ {
/* 13:37 */   private static final POILogger log = POILogFactory.getLogger(XSSFBRelation.class);
/* 14:39 */   static final XSSFBRelation SHARED_STRINGS_BINARY = new XSSFBRelation("application/vnd.ms-excel.sharedStrings", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/sharedStrings", "/xl/sharedStrings.bin", null);
/* 15:46 */   public static final XSSFBRelation STYLES_BINARY = new XSSFBRelation("application/vnd.ms-excel.styles", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles", "/xl/styles.bin", null);
/* 16:   */   
/* 17:   */   private XSSFBRelation(String type, String rel, String defaultName, Class<? extends POIXMLDocumentPart> cls)
/* 18:   */   {
/* 19:54 */     super(type, rel, defaultName, cls);
/* 20:   */   }
/* 21:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBRelation
 * JD-Core Version:    0.7.0.1
 */