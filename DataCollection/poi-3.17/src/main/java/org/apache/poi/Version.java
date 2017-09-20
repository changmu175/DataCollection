/*  1:   */ package org.apache.poi;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ 
/*  5:   */ public class Version
/*  6:   */ {
/*  7:   */   private static final String VERSION_STRING = "3.17";
/*  8:   */   private static final String RELEASE_DATE = "20170915";
/*  9:   */   
/* 10:   */   public static String getVersion()
/* 11:   */   {
/* 12:37 */     return "3.17";
/* 13:   */   }
/* 14:   */   
/* 15:   */   public static String getReleaseDate()
/* 16:   */   {
/* 17:44 */     return "20170915";
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static String getProduct()
/* 21:   */   {
/* 22:51 */     return "POI";
/* 23:   */   }
/* 24:   */   
/* 25:   */   public static String getImplementationLanguage()
/* 26:   */   {
/* 27:57 */     return "Java";
/* 28:   */   }
/* 29:   */   
/* 30:   */   public static void main(String[] args)
/* 31:   */   {
/* 32:64 */     System.out.println("Apache " + getProduct() + " " + getVersion() + " (" + getReleaseDate() + ")");
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.Version
 * JD-Core Version:    0.7.0.1
 */