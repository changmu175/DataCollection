/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.TwoDEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.BlankEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  8:   */ 
/*  9:   */ public final class Counta
/* 10:   */   implements Function
/* 11:   */ {
/* 12:   */   private final CountUtils.I_MatchPredicate _predicate;
/* 13:   */   
/* 14:   */   public Counta()
/* 15:   */   {
/* 16:41 */     this._predicate = defaultPredicate;
/* 17:   */   }
/* 18:   */   
/* 19:   */   private Counta(CountUtils.I_MatchPredicate criteriaPredicate)
/* 20:   */   {
/* 21:45 */     this._predicate = criteriaPredicate;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol)
/* 25:   */   {
/* 26:49 */     int nArgs = args.length;
/* 27:50 */     if (nArgs < 1) {
/* 28:52 */       return ErrorEval.VALUE_INVALID;
/* 29:   */     }
/* 30:55 */     if (nArgs > 30) {
/* 31:57 */       return ErrorEval.VALUE_INVALID;
/* 32:   */     }
/* 33:60 */     int temp = 0;
/* 34:62 */     for (int i = 0; i < nArgs; i++) {
/* 35:63 */       temp += CountUtils.countArg(args[i], this._predicate);
/* 36:   */     }
/* 37:66 */     return new NumberEval(temp);
/* 38:   */   }
/* 39:   */   
/* 40:69 */   private static final CountUtils.I_MatchPredicate defaultPredicate = new CountUtils.I_MatchPredicate()
/* 41:   */   {
/* 42:   */     public boolean matches(ValueEval valueEval)
/* 43:   */     {
/* 44:76 */       if (valueEval == BlankEval.instance) {
/* 45:77 */         return false;
/* 46:   */       }
/* 47:80 */       return true;
/* 48:   */     }
/* 49:   */   };
/* 50:83 */   private static final CountUtils.I_MatchPredicate subtotalPredicate = new CountUtils.I_MatchAreaPredicate()
/* 51:   */   {
/* 52:   */     public boolean matches(ValueEval valueEval)
/* 53:   */     {
/* 54:85 */       return Counta.defaultPredicate.matches(valueEval);
/* 55:   */     }
/* 56:   */     
/* 57:   */     public boolean matches(TwoDEval areEval, int rowIndex, int columnIndex)
/* 58:   */     {
/* 59:92 */       return !areEval.isSubTotal(rowIndex, columnIndex);
/* 60:   */     }
/* 61:   */   };
/* 62:   */   
/* 63:   */   public static Counta subtotalInstance()
/* 64:   */   {
/* 65:97 */     return new Counta(subtotalPredicate);
/* 66:   */   }
/* 67:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Counta
 * JD-Core Version:    0.7.0.1
 */