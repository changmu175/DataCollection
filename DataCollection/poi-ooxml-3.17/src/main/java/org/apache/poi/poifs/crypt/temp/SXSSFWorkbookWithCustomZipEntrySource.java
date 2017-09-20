/*  1:   */ package org.apache.poi.poifs.crypt.temp;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.OutputStream;
/*  5:   */ import java.security.GeneralSecurityException;
/*  6:   */ import org.apache.poi.openxml4j.util.ZipEntrySource;
/*  7:   */ import org.apache.poi.util.IOUtils;
/*  8:   */ import org.apache.poi.util.POILogFactory;
/*  9:   */ import org.apache.poi.util.POILogger;
/* 10:   */ import org.apache.poi.xssf.streaming.SXSSFWorkbook;
/* 11:   */ import org.apache.poi.xssf.streaming.SheetDataWriter;
/* 12:   */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/* 13:   */ 
/* 14:   */ public class SXSSFWorkbookWithCustomZipEntrySource
/* 15:   */   extends SXSSFWorkbook
/* 16:   */ {
/* 17:36 */   private static final POILogger LOG = POILogFactory.getLogger(SXSSFWorkbookWithCustomZipEntrySource.class);
/* 18:   */   
/* 19:   */   public SXSSFWorkbookWithCustomZipEntrySource()
/* 20:   */   {
/* 21:39 */     super(20);
/* 22:40 */     setCompressTempFiles(true);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void write(OutputStream stream)
/* 26:   */     throws IOException
/* 27:   */   {
/* 28:45 */     flushSheets();
/* 29:46 */     EncryptedTempData tempData = new EncryptedTempData();
/* 30:47 */     ZipEntrySource source = null;
/* 31:   */     try
/* 32:   */     {
/* 33:49 */       OutputStream os = tempData.getOutputStream();
/* 34:   */       try
/* 35:   */       {
/* 36:51 */         getXSSFWorkbook().write(os);
/* 37:   */       }
/* 38:   */       finally
/* 39:   */       {
/* 40:53 */         IOUtils.closeQuietly(os);
/* 41:   */       }
/* 42:56 */       source = AesZipFileZipEntrySource.createZipEntrySource(tempData.getInputStream());
/* 43:57 */       injectData(source, stream);
/* 44:   */     }
/* 45:   */     catch (GeneralSecurityException e)
/* 46:   */     {
/* 47:59 */       throw new IOException(e);
/* 48:   */     }
/* 49:   */     finally
/* 50:   */     {
/* 51:61 */       tempData.dispose();
/* 52:62 */       IOUtils.closeQuietly(source);
/* 53:   */     }
/* 54:   */   }
/* 55:   */   
/* 56:   */   protected SheetDataWriter createSheetDataWriter()
/* 57:   */     throws IOException
/* 58:   */   {
/* 59:69 */     LOG.log(3, new Object[] { "isCompressTempFiles: " + isCompressTempFiles() });
/* 60:70 */     LOG.log(3, new Object[] { "SharedStringSource: " + getSharedStringSource() });
/* 61:71 */     return new SheetDataWriterWithDecorator();
/* 62:   */   }
/* 63:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.temp.SXSSFWorkbookWithCustomZipEntrySource
 * JD-Core Version:    0.7.0.1
 */