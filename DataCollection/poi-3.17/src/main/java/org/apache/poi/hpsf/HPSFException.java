/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ public class HPSFException
/*  4:   */   extends Exception
/*  5:   */ {
/*  6:   */   private Throwable reason;
/*  7:   */   
/*  8:   */   public HPSFException() {}
/*  9:   */   
/* 10:   */   public HPSFException(String msg)
/* 11:   */   {
/* 12:53 */     super(msg);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public HPSFException(Throwable reason)
/* 16:   */   {
/* 17:67 */     this.reason = reason;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public HPSFException(String msg, Throwable reason)
/* 21:   */   {
/* 22:82 */     super(msg);
/* 23:83 */     this.reason = reason;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public Throwable getReason()
/* 27:   */   {
/* 28:97 */     return this.reason;
/* 29:   */   }
/* 30:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.HPSFException
 * JD-Core Version:    0.7.0.1
 */