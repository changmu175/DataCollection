/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ 
/*  5:   */ public class SystemOutLogger
/*  6:   */   extends POILogger
/*  7:   */ {
/*  8:   */   private String _cat;
/*  9:   */   
/* 10:   */   public void initialize(String cat)
/* 11:   */   {
/* 12:35 */     this._cat = cat;
/* 13:   */   }
/* 14:   */   
/* 15:   */   protected void _log(int level, Object obj1)
/* 16:   */   {
/* 17:47 */     _log(level, obj1, null);
/* 18:   */   }
/* 19:   */   
/* 20:   */   @SuppressForbidden("uses printStackTrace")
/* 21:   */   protected void _log(int level, Object obj1, Throwable exception)
/* 22:   */   {
/* 23:61 */     if (check(level))
/* 24:   */     {
/* 25:62 */       System.out.println("[" + this._cat + "]" + LEVEL_STRINGS_SHORT[Math.min(LEVEL_STRINGS_SHORT.length - 1, level)] + " " + obj1);
/* 26:63 */       if (exception != null) {
/* 27:64 */         exception.printStackTrace(System.out);
/* 28:   */       }
/* 29:   */     }
/* 30:   */   }
/* 31:   */   
/* 32:   */   public boolean check(int level)
/* 33:   */   {
/* 34:   */     int currentLevel;
/* 35:   */     try
/* 36:   */     {
/* 37:84 */       currentLevel = Integer.parseInt(System.getProperty("poi.log.level", "5"));
/* 38:   */     }
/* 39:   */     catch (SecurityException e)
/* 40:   */     {
/* 41:86 */       currentLevel = 1;
/* 42:   */     }
/* 43:89 */     return level >= currentLevel;
/* 44:   */   }
/* 45:   */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.util.SystemOutLogger

 * JD-Core Version:    0.7.0.1

 */