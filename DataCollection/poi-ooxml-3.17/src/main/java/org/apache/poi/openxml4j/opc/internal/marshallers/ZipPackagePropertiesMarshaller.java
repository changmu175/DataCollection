/*  1:   */ package org.apache.poi.openxml4j.opc.internal.marshallers;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.OutputStream;
/*  5:   */ import java.net.URI;
/*  6:   */ import java.util.zip.ZipEntry;
/*  7:   */ import java.util.zip.ZipOutputStream;
/*  8:   */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  9:   */ import org.apache.poi.openxml4j.opc.PackagePart;
/* 10:   */ import org.apache.poi.openxml4j.opc.PackagePartName;
/* 11:   */ import org.apache.poi.openxml4j.opc.StreamHelper;
/* 12:   */ import org.apache.poi.openxml4j.opc.internal.ZipHelper;
/* 13:   */ 
/* 14:   */ public final class ZipPackagePropertiesMarshaller
/* 15:   */   extends PackagePropertiesMarshaller
/* 16:   */ {
/* 17:   */   public boolean marshall(PackagePart part, OutputStream out)
/* 18:   */     throws OpenXML4JException
/* 19:   */   {
/* 20:40 */     if (!(out instanceof ZipOutputStream)) {
/* 21:41 */       throw new IllegalArgumentException("ZipOutputStream expected!");
/* 22:   */     }
/* 23:43 */     ZipOutputStream zos = (ZipOutputStream)out;
/* 24:   */     
/* 25:   */ 
/* 26:46 */     ZipEntry ctEntry = new ZipEntry(ZipHelper.getZipItemNameFromOPCName(part.getPartName().getURI().toString()));
/* 27:   */     try
/* 28:   */     {
/* 29:51 */       zos.putNextEntry(ctEntry);
/* 30:52 */       super.marshall(part, out);
/* 31:54 */       if (!StreamHelper.saveXmlInStream(this.xmlDoc, out)) {
/* 32:55 */         return false;
/* 33:   */       }
/* 34:57 */       zos.closeEntry();
/* 35:   */     }
/* 36:   */     catch (IOException e)
/* 37:   */     {
/* 38:59 */       throw new OpenXML4JException(e.getLocalizedMessage(), e);
/* 39:   */     }
/* 40:61 */     return true;
/* 41:   */   }
/* 42:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.marshallers.ZipPackagePropertiesMarshaller
 * JD-Core Version:    0.7.0.1
 */