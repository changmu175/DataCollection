/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ @Internal
/*   4:    */ public abstract class POILogger
/*   5:    */ {
/*   6:    */   public static final int DEBUG = 1;
/*   7:    */   public static final int INFO = 3;
/*   8:    */   public static final int WARN = 5;
/*   9:    */   public static final int ERROR = 7;
/*  10:    */   public static final int FATAL = 9;
/*  11: 36 */   protected static final String[] LEVEL_STRINGS_SHORT = { "?", "D", "?", "I", "?", "W", "?", "E", "?", "F", "?" };
/*  12: 38 */   protected static final String[] LEVEL_STRINGS = { "?0?", "DEBUG", "?2?", "INFO", "?4?", "WARN", "?6?", "ERROR", "?8?", "FATAL", "?10+?" };
/*  13:    */   
/*  14:    */   public abstract void initialize(String paramString);
/*  15:    */   
/*  16:    */   protected abstract void _log(int paramInt, Object paramObject);
/*  17:    */   
/*  18:    */   protected abstract void _log(int paramInt, Object paramObject, Throwable paramThrowable);
/*  19:    */   
/*  20:    */   public abstract boolean check(int paramInt);
/*  21:    */   
/*  22:    */   public void log(int level, Object... objs)
/*  23:    */   {
/*  24: 93 */     if (!check(level)) {
/*  25: 93 */       return;
/*  26:    */     }
/*  27: 94 */     StringBuilder sb = new StringBuilder(32);
/*  28: 95 */     Throwable lastEx = null;
/*  29: 96 */     for (int i = 0; i < objs.length; i++) {
/*  30: 97 */       if ((i == objs.length - 1) && ((objs[i] instanceof Throwable))) {
/*  31: 98 */         lastEx = (Throwable)objs[i];
/*  32:    */       } else {
/*  33:100 */         sb.append(objs[i]);
/*  34:    */       }
/*  35:    */     }
/*  36:104 */     String msg = sb.toString();
/*  37:105 */     msg = msg.replaceAll("[\r\n]+", " ");
/*  38:109 */     if (lastEx == null) {
/*  39:110 */       _log(level, msg);
/*  40:    */     } else {
/*  41:112 */       _log(level, msg, lastEx);
/*  42:    */     }
/*  43:    */   }
/*  44:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.POILogger
 * JD-Core Version:    0.7.0.1
 */