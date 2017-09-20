/*  1:   */ package org.apache.poi.xssf.streaming;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.FileInputStream;
/*  5:   */ import java.io.FileOutputStream;
/*  6:   */ import java.io.IOException;
/*  7:   */ import java.io.InputStream;
/*  8:   */ import java.io.OutputStream;
/*  9:   */ import java.util.zip.GZIPInputStream;
/* 10:   */ import java.util.zip.GZIPOutputStream;
/* 11:   */ import org.apache.poi.util.TempFile;
/* 12:   */ import org.apache.poi.xssf.model.SharedStringsTable;
/* 13:   */ 
/* 14:   */ public class GZIPSheetDataWriter
/* 15:   */   extends SheetDataWriter
/* 16:   */ {
/* 17:   */   public GZIPSheetDataWriter()
/* 18:   */     throws IOException
/* 19:   */   {}
/* 20:   */   
/* 21:   */   public GZIPSheetDataWriter(SharedStringsTable sharedStringsTable)
/* 22:   */     throws IOException
/* 23:   */   {
/* 24:47 */     super(sharedStringsTable);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public File createTempFile()
/* 28:   */     throws IOException
/* 29:   */   {
/* 30:55 */     return TempFile.createTempFile("poi-sxssf-sheet-xml", ".gz");
/* 31:   */   }
/* 32:   */   
/* 33:   */   protected InputStream decorateInputStream(FileInputStream fis)
/* 34:   */     throws IOException
/* 35:   */   {
/* 36:60 */     return new GZIPInputStream(fis);
/* 37:   */   }
/* 38:   */   
/* 39:   */   protected OutputStream decorateOutputStream(FileOutputStream fos)
/* 40:   */     throws IOException
/* 41:   */   {
/* 42:65 */     return new GZIPOutputStream(fos);
/* 43:   */   }
/* 44:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.streaming.GZIPSheetDataWriter
 * JD-Core Version:    0.7.0.1
 */