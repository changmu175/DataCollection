/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ public class IllegalPropertySetDataException
/*  4:   */   extends HPSFRuntimeException
/*  5:   */ {
/*  6:   */   public IllegalPropertySetDataException() {}
/*  7:   */   
/*  8:   */   public IllegalPropertySetDataException(String msg)
/*  9:   */   {
/* 10:50 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public IllegalPropertySetDataException(Throwable reason)
/* 14:   */   {
/* 15:62 */     super(reason);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public IllegalPropertySetDataException(String msg, Throwable reason)
/* 19:   */   {
/* 20:76 */     super(msg, reason);
/* 21:   */   }
/* 22:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.IllegalPropertySetDataException
 * JD-Core Version:    0.7.0.1
 */