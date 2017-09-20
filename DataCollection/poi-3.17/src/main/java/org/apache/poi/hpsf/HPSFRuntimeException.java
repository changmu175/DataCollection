/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ public class HPSFRuntimeException
/*  4:   */   extends RuntimeException
/*  5:   */ {
/*  6:   */   private static final long serialVersionUID = -7804271670232727159L;
/*  7:   */   private Throwable reason;
/*  8:   */   
/*  9:   */   public HPSFRuntimeException() {}
/* 10:   */   
/* 11:   */   public HPSFRuntimeException(String msg)
/* 12:   */   {
/* 13:53 */     super(msg);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public HPSFRuntimeException(Throwable reason)
/* 17:   */   {
/* 18:68 */     this.reason = reason;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public HPSFRuntimeException(String msg, Throwable reason)
/* 22:   */   {
/* 23:83 */     super(msg);
/* 24:84 */     this.reason = reason;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Throwable getReason()
/* 28:   */   {
/* 29:98 */     return this.reason;
/* 30:   */   }
/* 31:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.HPSFRuntimeException
 * JD-Core Version:    0.7.0.1
 */