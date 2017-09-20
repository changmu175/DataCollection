/*  1:   */ package org.apache.poi.hssf.dev;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayOutputStream;
/*  4:   */ import java.io.FileInputStream;
/*  5:   */ import java.io.FileOutputStream;
/*  6:   */ import java.io.OutputStream;
/*  7:   */ import java.io.PrintStream;
/*  8:   */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*  9:   */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/* 10:   */ 
/* 11:   */ public class ReSave
/* 12:   */ {
/* 13:   */   public static void main(String[] args)
/* 14:   */     throws Exception
/* 15:   */   {
/* 16:38 */     boolean initDrawing = false;
/* 17:39 */     boolean saveToMemory = false;
/* 18:40 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 19:41 */     for (String filename : args) {
/* 20:42 */       if (filename.equals("-dg"))
/* 21:   */       {
/* 22:43 */         initDrawing = true;
/* 23:   */       }
/* 24:44 */       else if (filename.equals("-bos"))
/* 25:   */       {
/* 26:45 */         saveToMemory = true;
/* 27:   */       }
/* 28:   */       else
/* 29:   */       {
/* 30:47 */         System.out.print("reading " + filename + "...");
/* 31:48 */         FileInputStream is = new FileInputStream(filename);
/* 32:49 */         HSSFWorkbook wb = new HSSFWorkbook(is);
/* 33:   */         try
/* 34:   */         {
/* 35:51 */           System.out.println("done");
/* 36:53 */           for (int i = 0; i < wb.getNumberOfSheets(); i++)
/* 37:   */           {
/* 38:54 */             HSSFSheet sheet = wb.getSheetAt(i);
/* 39:55 */             if (initDrawing) {
/* 40:56 */               sheet.getDrawingPatriarch();
/* 41:   */             }
/* 42:   */           }
/* 43:   */           OutputStream os;
/* 44:   */           OutputStream os;
/* 45:61 */           if (saveToMemory)
/* 46:   */           {
/* 47:62 */             bos.reset();
/* 48:63 */             os = bos;
/* 49:   */           }
/* 50:   */           else
/* 51:   */           {
/* 52:65 */             String outputFile = filename.replace(".xls", "-saved.xls");
/* 53:66 */             System.out.print("saving to " + outputFile + "...");
/* 54:67 */             os = new FileOutputStream(outputFile);
/* 55:   */           }
/* 56:   */           try
/* 57:   */           {
/* 58:71 */             wb.write(os);
/* 59:   */           }
/* 60:   */           finally
/* 61:   */           {
/* 62:73 */             os.close();
/* 63:   */           }
/* 64:75 */           System.out.println("done");
/* 65:   */         }
/* 66:   */         finally
/* 67:   */         {
/* 68:77 */           wb.close();
/* 69:78 */           is.close();
/* 70:   */         }
/* 71:   */       }
/* 72:   */     }
/* 73:   */   }
/* 74:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.dev.ReSave
 * JD-Core Version:    0.7.0.1
 */