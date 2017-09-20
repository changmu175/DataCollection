/*  1:   */ package org.apache.poi.openxml4j.opc.internal;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import java.io.OutputStream;
/*  6:   */ import java.util.zip.ZipEntry;
/*  7:   */ import java.util.zip.ZipOutputStream;
/*  8:   */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  9:   */ import org.apache.poi.openxml4j.opc.OPCPackage;
/* 10:   */ import org.apache.poi.openxml4j.opc.StreamHelper;
/* 11:   */ import org.apache.poi.util.POILogFactory;
/* 12:   */ import org.apache.poi.util.POILogger;
/* 13:   */ import org.w3c.dom.Document;
/* 14:   */ 
/* 15:   */ public class ZipContentTypeManager
/* 16:   */   extends ContentTypeManager
/* 17:   */ {
/* 18:41 */   private static final POILogger logger = POILogFactory.getLogger(ZipContentTypeManager.class);
/* 19:   */   
/* 20:   */   public ZipContentTypeManager(InputStream in, OPCPackage pkg)
/* 21:   */     throws InvalidFormatException
/* 22:   */   {
/* 23:54 */     super(in, pkg);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean saveImpl(Document content, OutputStream out)
/* 27:   */   {
/* 28:60 */     ZipOutputStream zos = null;
/* 29:61 */     if ((out instanceof ZipOutputStream)) {
/* 30:62 */       zos = (ZipOutputStream)out;
/* 31:   */     } else {
/* 32:64 */       zos = new ZipOutputStream(out);
/* 33:   */     }
/* 34:66 */     ZipEntry partEntry = new ZipEntry("[Content_Types].xml");
/* 35:   */     try
/* 36:   */     {
/* 37:69 */       zos.putNextEntry(partEntry);
/* 38:71 */       if (!StreamHelper.saveXmlInStream(content, zos)) {
/* 39:72 */         return false;
/* 40:   */       }
/* 41:74 */       zos.closeEntry();
/* 42:   */     }
/* 43:   */     catch (IOException ioe)
/* 44:   */     {
/* 45:76 */       logger.log(7, new Object[] { "Cannot write: [Content_Types].xml in Zip !", ioe });
/* 46:   */       
/* 47:78 */       return false;
/* 48:   */     }
/* 49:80 */     return true;
/* 50:   */   }
/* 51:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.ZipContentTypeManager
 * JD-Core Version:    0.7.0.1
 */