/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ public class RecordFormatException
/*  4:   */   extends RuntimeException
/*  5:   */ {
/*  6:   */   public RecordFormatException(String exception)
/*  7:   */   {
/*  8:32 */     super(exception);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public RecordFormatException(String exception, Throwable thr)
/* 12:   */   {
/* 13:36 */     super(exception, thr);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public RecordFormatException(Throwable thr)
/* 17:   */   {
/* 18:40 */     super(thr);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public static void check(boolean assertTrue, String message)
/* 22:   */   {
/* 23:52 */     if (!assertTrue) {
/* 24:53 */       throw new RecordFormatException(message);
/* 25:   */     }
/* 26:   */   }
/* 27:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.RecordFormatException
 * JD-Core Version:    0.7.0.1
 */