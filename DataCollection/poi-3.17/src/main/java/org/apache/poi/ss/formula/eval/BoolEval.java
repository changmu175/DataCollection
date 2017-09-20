/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ public final class BoolEval
/*  4:   */   implements NumericValueEval, StringValueEval
/*  5:   */ {
/*  6:   */   private boolean _value;
/*  7:27 */   public static final BoolEval FALSE = new BoolEval(false);
/*  8:29 */   public static final BoolEval TRUE = new BoolEval(true);
/*  9:   */   
/* 10:   */   public static BoolEval valueOf(boolean b)
/* 11:   */   {
/* 12:38 */     return b ? TRUE : FALSE;
/* 13:   */   }
/* 14:   */   
/* 15:   */   private BoolEval(boolean value)
/* 16:   */   {
/* 17:42 */     this._value = value;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public boolean getBooleanValue()
/* 21:   */   {
/* 22:46 */     return this._value;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public double getNumberValue()
/* 26:   */   {
/* 27:50 */     return this._value ? 1.0D : 0.0D;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String getStringValue()
/* 31:   */   {
/* 32:54 */     return this._value ? "TRUE" : "FALSE";
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String toString()
/* 36:   */   {
/* 37:58 */     return getClass().getName() + " [" + getStringValue() + "]";
/* 38:   */   }
/* 39:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.BoolEval
 * JD-Core Version:    0.7.0.1
 */