/*   1:    */ package org.apache.poi.hssf.eventusermodel;
/*   2:    */ 
/*   3:    */ public class HSSFUserException
/*   4:    */   extends Exception
/*   5:    */ {
/*   6:    */   private Throwable reason;
/*   7:    */   
/*   8:    */   public HSSFUserException() {}
/*   9:    */   
/*  10:    */   public HSSFUserException(String msg)
/*  11:    */   {
/*  12: 62 */     super(msg);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public HSSFUserException(Throwable reason)
/*  16:    */   {
/*  17: 75 */     this.reason = reason;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public HSSFUserException(String msg, Throwable reason)
/*  21:    */   {
/*  22: 89 */     super(msg);
/*  23: 90 */     this.reason = reason;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public Throwable getReason()
/*  27:    */   {
/*  28:104 */     return this.reason;
/*  29:    */   }
/*  30:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.eventusermodel.HSSFUserException
 * JD-Core Version:    0.7.0.1
 */