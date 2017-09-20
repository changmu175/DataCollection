/*  1:   */ package org.apache.poi.xssf.dev;
/*  2:   */ 
/*  3:   */ import java.io.FileOutputStream;
/*  4:   */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  5:   */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*  6:   */ 
/*  7:   */ public final class XSSFSave
/*  8:   */ {
/*  9:   */   public static void main(String[] args)
/* 10:   */     throws Exception
/* 11:   */   {
/* 12:33 */     for (int i = 0; i < args.length; i++)
/* 13:   */     {
/* 14:34 */       OPCPackage pkg = OPCPackage.open(args[i]);
/* 15:35 */       XSSFWorkbook wb = new XSSFWorkbook(pkg);
/* 16:   */       
/* 17:37 */       int sep = args[i].lastIndexOf('.');
/* 18:38 */       String outfile = args[i].substring(0, sep) + "-save.xls" + (wb.isMacroEnabled() ? "m" : "x");
/* 19:39 */       FileOutputStream out = new FileOutputStream(outfile);
/* 20:40 */       wb.write(out);
/* 21:41 */       out.close();
/* 22:   */       
/* 23:43 */       pkg.close();
/* 24:   */     }
/* 25:   */   }
/* 26:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.dev.XSSFSave
 * JD-Core Version:    0.7.0.1
 */