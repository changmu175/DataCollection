/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ public class NoSingleSectionException
/*  4:   */   extends HPSFRuntimeException
/*  5:   */ {
/*  6:   */   public NoSingleSectionException() {}
/*  7:   */   
/*  8:   */   public NoSingleSectionException(String msg)
/*  9:   */   {
/* 10:48 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public NoSingleSectionException(Throwable reason)
/* 14:   */   {
/* 15:59 */     super(reason);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public NoSingleSectionException(String msg, Throwable reason)
/* 19:   */   {
/* 20:71 */     super(msg, reason);
/* 21:   */   }
/* 22:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.NoSingleSectionException
 * JD-Core Version:    0.7.0.1
 */