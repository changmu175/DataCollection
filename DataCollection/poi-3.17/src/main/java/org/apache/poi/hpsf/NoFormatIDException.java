/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ public class NoFormatIDException
/*  4:   */   extends HPSFRuntimeException
/*  5:   */ {
/*  6:   */   public NoFormatIDException() {}
/*  7:   */   
/*  8:   */   public NoFormatIDException(String msg)
/*  9:   */   {
/* 10:41 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public NoFormatIDException(Throwable reason)
/* 14:   */   {
/* 15:51 */     super(reason);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public NoFormatIDException(String msg, Throwable reason)
/* 19:   */   {
/* 20:62 */     super(msg, reason);
/* 21:   */   }
/* 22:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.NoFormatIDException
 * JD-Core Version:    0.7.0.1
 */