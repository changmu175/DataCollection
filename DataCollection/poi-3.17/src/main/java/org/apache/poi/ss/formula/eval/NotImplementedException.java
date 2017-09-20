/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ public class NotImplementedException
/*  4:   */   extends RuntimeException
/*  5:   */ {
/*  6:   */   private static final long serialVersionUID = -5840703336495141301L;
/*  7:   */   
/*  8:   */   public NotImplementedException(String message)
/*  9:   */   {
/* 10:35 */     super(message);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public NotImplementedException(String message, NotImplementedException cause)
/* 14:   */   {
/* 15:38 */     super(message, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.NotImplementedException
 * JD-Core Version:    0.7.0.1
 */