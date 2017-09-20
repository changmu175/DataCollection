/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.OutputStreamWriter;
/*  6:   */ import java.io.PrintWriter;
/*  7:   */ import java.nio.charset.Charset;
/*  8:   */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*  9:   */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/* 10:   */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/* 11:   */ import org.apache.poi.ss.usermodel.Sheet;
/* 12:   */ 
/* 13:   */ public class DrawingDump
/* 14:   */ {
/* 15:   */   public static void main(String[] args)
/* 16:   */     throws IOException
/* 17:   */   {
/* 18:38 */     OutputStreamWriter osw = new OutputStreamWriter(System.out, Charset.defaultCharset());
/* 19:39 */     PrintWriter pw = new PrintWriter(osw);
/* 20:40 */     NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(args[0]));
/* 21:41 */     HSSFWorkbook wb = new HSSFWorkbook(fs);
/* 22:   */     try
/* 23:   */     {
/* 24:43 */       pw.println("Drawing group:");
/* 25:44 */       wb.dumpDrawingGroupRecords(true);
/* 26:   */       
/* 27:46 */       i = 1;
/* 28:47 */       for (Sheet sheet : wb)
/* 29:   */       {
/* 30:49 */         pw.println("Sheet " + i + "(" + sheet.getSheetName() + "):");
/* 31:50 */         ((HSSFSheet)sheet).dumpDrawingRecords(true, pw);
/* 32:   */       }
/* 33:   */     }
/* 34:   */     finally
/* 35:   */     {
/* 36:   */       int i;
/* 37:53 */       wb.close();
/* 38:54 */       fs.close();
/* 39:   */     }
/* 40:   */   }
/* 41:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.DrawingDump
 * JD-Core Version:    0.7.0.1
 */