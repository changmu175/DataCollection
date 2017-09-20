/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ public class DocumentFormatException
/*  4:   */   extends RuntimeException
/*  5:   */ {
/*  6:   */   public DocumentFormatException(String exception)
/*  7:   */   {
/*  8:29 */     super(exception);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public DocumentFormatException(String exception, Throwable thr)
/* 12:   */   {
/* 13:33 */     super(exception, thr);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public DocumentFormatException(Throwable thr)
/* 17:   */   {
/* 18:37 */     super(thr);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public static void check(boolean assertTrue, String message)
/* 22:   */   {
/* 23:49 */     if (!assertTrue) {
/* 24:50 */       throw new DocumentFormatException(message);
/* 25:   */     }
/* 26:   */   }
/* 27:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.DocumentFormatException
 * JD-Core Version:    0.7.0.1
 */