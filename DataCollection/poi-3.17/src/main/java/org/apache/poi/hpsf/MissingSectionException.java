/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ public class MissingSectionException
/*  4:   */   extends HPSFRuntimeException
/*  5:   */ {
/*  6:   */   public MissingSectionException() {}
/*  7:   */   
/*  8:   */   public MissingSectionException(String msg)
/*  9:   */   {
/* 10:46 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public MissingSectionException(Throwable reason)
/* 14:   */   {
/* 15:57 */     super(reason);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public MissingSectionException(String msg, Throwable reason)
/* 19:   */   {
/* 20:69 */     super(msg, reason);
/* 21:   */   }
/* 22:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.MissingSectionException
 * JD-Core Version:    0.7.0.1
 */