/*  1:   */ package org.apache.poi.poifs.dev;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.PrintStream;
/*  6:   */ import java.util.List;
/*  7:   */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  8:   */ 
/*  9:   */ public class POIFSViewer
/* 10:   */ {
/* 11:   */   public static void main(String[] args)
/* 12:   */   {
/* 13:44 */     if (args.length == 0)
/* 14:   */     {
/* 15:45 */       System.err.println("Must specify at least one file to view");
/* 16:46 */       System.exit(1);
/* 17:   */     }
/* 18:48 */     boolean printNames = args.length > 1;
/* 19:50 */     for (int j = 0; j < args.length; j++) {
/* 20:52 */       viewFile(args[j], printNames);
/* 21:   */     }
/* 22:   */   }
/* 23:   */   
/* 24:   */   private static void viewFile(String filename, boolean printName)
/* 25:   */   {
/* 26:57 */     if (printName)
/* 27:   */     {
/* 28:58 */       StringBuffer flowerbox = new StringBuffer();
/* 29:   */       
/* 30:60 */       flowerbox.append(".");
/* 31:61 */       for (int j = 0; j < filename.length(); j++) {
/* 32:62 */         flowerbox.append("-");
/* 33:   */       }
/* 34:64 */       flowerbox.append(".");
/* 35:65 */       System.out.println(flowerbox);
/* 36:66 */       System.out.println("|" + filename + "|");
/* 37:67 */       System.out.println(flowerbox);
/* 38:   */     }
/* 39:   */     try
/* 40:   */     {
/* 41:70 */       NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(filename));
/* 42:71 */       List<String> strings = POIFSViewEngine.inspectViewable(fs, true, 0, "  ");
/* 43:72 */       for (String s : strings) {
/* 44:73 */         System.out.print(s);
/* 45:   */       }
/* 46:75 */       fs.close();
/* 47:   */     }
/* 48:   */     catch (IOException e)
/* 49:   */     {
/* 50:77 */       System.out.println(e.getMessage());
/* 51:   */     }
/* 52:   */   }
/* 53:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.dev.POIFSViewer
 * JD-Core Version:    0.7.0.1
 */