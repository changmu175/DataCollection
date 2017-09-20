/*  1:   */ package org.apache.poi.extractor;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.PrintStream;
/*  5:   */ import org.apache.poi.POITextExtractor;
/*  6:   */ 
/*  7:   */ public class CommandLineTextExtractor
/*  8:   */ {
/*  9:   */   public static final String DIVIDER = "=======================";
/* 10:   */   
/* 11:   */   public static void main(String[] args)
/* 12:   */     throws Exception
/* 13:   */   {
/* 14:31 */     if (args.length < 1)
/* 15:   */     {
/* 16:32 */       System.err.println("Use:");
/* 17:33 */       System.err.println("   CommandLineTextExtractor <filename> [filename] [filename]");
/* 18:34 */       System.exit(1);
/* 19:   */     }
/* 20:37 */     for (String arg : args)
/* 21:   */     {
/* 22:38 */       System.out.println("=======================");
/* 23:   */       
/* 24:40 */       File f = new File(arg);
/* 25:41 */       System.out.println(f);
/* 26:   */       
/* 27:43 */       POITextExtractor extractor = ExtractorFactory.createExtractor(f);
/* 28:   */       try
/* 29:   */       {
/* 30:46 */         POITextExtractor metadataExtractor = extractor.getMetadataTextExtractor();
/* 31:   */         
/* 32:   */ 
/* 33:49 */         System.out.println("   =======================");
/* 34:50 */         String metaData = metadataExtractor.getText();
/* 35:51 */         System.out.println(metaData);
/* 36:52 */         System.out.println("   =======================");
/* 37:53 */         String text = extractor.getText();
/* 38:54 */         System.out.println(text);
/* 39:55 */         System.out.println("=======================");
/* 40:56 */         System.out.println("Had " + metaData.length() + " characters of metadata and " + text.length() + " characters of text");
/* 41:   */       }
/* 42:   */       finally
/* 43:   */       {
/* 44:58 */         extractor.close();
/* 45:   */       }
/* 46:   */     }
/* 47:   */   }
/* 48:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.extractor.CommandLineTextExtractor
 * JD-Core Version:    0.7.0.1
 */