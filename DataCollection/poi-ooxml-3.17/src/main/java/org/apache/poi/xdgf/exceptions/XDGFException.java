/*  1:   */ package org.apache.poi.xdgf.exceptions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.POIXMLDocumentPart;
/*  4:   */ import org.apache.poi.POIXMLException;
/*  5:   */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  6:   */ 
/*  7:   */ public class XDGFException
/*  8:   */ {
/*  9:   */   public static POIXMLException error(String message, Object o)
/* 10:   */   {
/* 11:29 */     return new POIXMLException(o + ": " + message);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public static POIXMLException error(String message, Object o, Throwable t)
/* 15:   */   {
/* 16:33 */     return new POIXMLException(o + ": " + message, t);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public static POIXMLException wrap(POIXMLDocumentPart part, POIXMLException e)
/* 20:   */   {
/* 21:43 */     return new POIXMLException(part.getPackagePart().getPartName() + ": " + e.getMessage(), e.getCause() == null ? e : e.getCause());
/* 22:   */   }
/* 23:   */   
/* 24:   */   public static POIXMLException wrap(String where, POIXMLException e)
/* 25:   */   {
/* 26:49 */     return new POIXMLException(where + ": " + e.getMessage(), e.getCause() == null ? e : e.getCause());
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.exceptions.XDGFException
 * JD-Core Version:    0.7.0.1
 */