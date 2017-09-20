/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.ptg.IntPtg;
/*  4:   */ import org.apache.poi.ss.formula.ptg.NumberPtg;
/*  5:   */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  6:   */ import org.apache.poi.ss.util.NumberToTextConverter;
/*  7:   */ 
/*  8:   */ public final class NumberEval
/*  9:   */   implements NumericValueEval, StringValueEval
/* 10:   */ {
/* 11:34 */   public static final NumberEval ZERO = new NumberEval(0.0D);
/* 12:   */   private final double _value;
/* 13:   */   private String _stringValue;
/* 14:   */   
/* 15:   */   public NumberEval(Ptg ptg)
/* 16:   */   {
/* 17:40 */     if (ptg == null) {
/* 18:41 */       throw new IllegalArgumentException("ptg must not be null");
/* 19:   */     }
/* 20:43 */     if ((ptg instanceof IntPtg)) {
/* 21:44 */       this._value = ((IntPtg)ptg).getValue();
/* 22:45 */     } else if ((ptg instanceof NumberPtg)) {
/* 23:46 */       this._value = ((NumberPtg)ptg).getValue();
/* 24:   */     } else {
/* 25:48 */       throw new IllegalArgumentException("bad argument type (" + ptg.getClass().getName() + ")");
/* 26:   */     }
/* 27:   */   }
/* 28:   */   
/* 29:   */   public NumberEval(double value)
/* 30:   */   {
/* 31:53 */     this._value = value;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public double getNumberValue()
/* 35:   */   {
/* 36:57 */     return this._value;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public String getStringValue()
/* 40:   */   {
/* 41:61 */     if (this._stringValue == null) {
/* 42:62 */       this._stringValue = NumberToTextConverter.toText(this._value);
/* 43:   */     }
/* 44:64 */     return this._stringValue;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public final String toString()
/* 48:   */   {
/* 49:67 */     return getClass().getName() + " [" + getStringValue() + "]";
/* 50:   */   }
/* 51:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.NumberEval
 * JD-Core Version:    0.7.0.1
 */