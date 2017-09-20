/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.TwoDEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.MissingArgEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   8:    */ 
/*   9:    */ public final class Count
/*  10:    */   implements Function
/*  11:    */ {
/*  12:    */   private final CountUtils.I_MatchPredicate _predicate;
/*  13:    */   
/*  14:    */   public Count()
/*  15:    */   {
/*  16: 43 */     this._predicate = defaultPredicate;
/*  17:    */   }
/*  18:    */   
/*  19:    */   private Count(CountUtils.I_MatchPredicate criteriaPredicate)
/*  20:    */   {
/*  21: 47 */     this._predicate = criteriaPredicate;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol)
/*  25:    */   {
/*  26: 51 */     int nArgs = args.length;
/*  27: 52 */     if (nArgs < 1) {
/*  28: 54 */       return ErrorEval.VALUE_INVALID;
/*  29:    */     }
/*  30: 57 */     if (nArgs > 30) {
/*  31: 59 */       return ErrorEval.VALUE_INVALID;
/*  32:    */     }
/*  33: 62 */     int temp = 0;
/*  34: 64 */     for (int i = 0; i < nArgs; i++) {
/*  35: 65 */       temp += CountUtils.countArg(args[i], this._predicate);
/*  36:    */     }
/*  37: 68 */     return new NumberEval(temp);
/*  38:    */   }
/*  39:    */   
/*  40: 71 */   private static final CountUtils.I_MatchPredicate defaultPredicate = new CountUtils.I_MatchPredicate()
/*  41:    */   {
/*  42:    */     public boolean matches(ValueEval valueEval)
/*  43:    */     {
/*  44: 75 */       if ((valueEval instanceof NumberEval)) {
/*  45: 77 */         return true;
/*  46:    */       }
/*  47: 79 */       if (valueEval == MissingArgEval.instance) {
/*  48: 81 */         return true;
/*  49:    */       }
/*  50: 85 */       return false;
/*  51:    */     }
/*  52:    */   };
/*  53: 89 */   private static final CountUtils.I_MatchPredicate subtotalPredicate = new CountUtils.I_MatchAreaPredicate()
/*  54:    */   {
/*  55:    */     public boolean matches(ValueEval valueEval)
/*  56:    */     {
/*  57: 91 */       return Count.defaultPredicate.matches(valueEval);
/*  58:    */     }
/*  59:    */     
/*  60:    */     public boolean matches(TwoDEval areEval, int rowIndex, int columnIndex)
/*  61:    */     {
/*  62: 98 */       return !areEval.isSubTotal(rowIndex, columnIndex);
/*  63:    */     }
/*  64:    */   };
/*  65:    */   
/*  66:    */   public static Count subtotalInstance()
/*  67:    */   {
/*  68:112 */     return new Count(subtotalPredicate);
/*  69:    */   }
/*  70:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Count
 * JD-Core Version:    0.7.0.1
 */