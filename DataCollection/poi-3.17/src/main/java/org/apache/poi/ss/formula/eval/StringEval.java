/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  4:   */ import org.apache.poi.ss.formula.ptg.StringPtg;
/*  5:   */ 
/*  6:   */ public final class StringEval
/*  7:   */   implements StringValueEval
/*  8:   */ {
/*  9:28 */   public static final StringEval EMPTY_INSTANCE = new StringEval("");
/* 10:   */   private final String _value;
/* 11:   */   
/* 12:   */   public StringEval(Ptg ptg)
/* 13:   */   {
/* 14:33 */     this(((StringPtg)ptg).getValue());
/* 15:   */   }
/* 16:   */   
/* 17:   */   public StringEval(String value)
/* 18:   */   {
/* 19:37 */     if (value == null) {
/* 20:38 */       throw new IllegalArgumentException("value must not be null");
/* 21:   */     }
/* 22:40 */     this._value = value;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String getStringValue()
/* 26:   */   {
/* 27:44 */     return this._value;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String toString()
/* 31:   */   {
/* 32:48 */     return getClass().getName() + " [" + this._value + "]";
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.StringEval
 * JD-Core Version:    0.7.0.1
 */