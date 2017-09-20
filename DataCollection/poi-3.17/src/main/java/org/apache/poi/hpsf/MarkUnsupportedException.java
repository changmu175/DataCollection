/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ public class MarkUnsupportedException
/*  4:   */   extends HPSFException
/*  5:   */ {
/*  6:   */   public MarkUnsupportedException() {}
/*  7:   */   
/*  8:   */   public MarkUnsupportedException(String msg)
/*  9:   */   {
/* 10:43 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public MarkUnsupportedException(Throwable reason)
/* 14:   */   {
/* 15:54 */     super(reason);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public MarkUnsupportedException(String msg, Throwable reason)
/* 19:   */   {
/* 20:66 */     super(msg, reason);
/* 21:   */   }
/* 22:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.MarkUnsupportedException
 * JD-Core Version:    0.7.0.1
 */