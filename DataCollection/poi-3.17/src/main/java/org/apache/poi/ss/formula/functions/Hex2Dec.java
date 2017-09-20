/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  4:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  7:   */ import org.apache.poi.ss.formula.eval.RefEval;
/*  8:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  9:   */ 
/* 10:   */ public class Hex2Dec
/* 11:   */   extends Fixed1ArgFunction
/* 12:   */   implements FreeRefFunction
/* 13:   */ {
/* 14:40 */   public static final FreeRefFunction instance = new Hex2Dec();
/* 15:   */   static final int HEXADECIMAL_BASE = 16;
/* 16:   */   static final int MAX_NUMBER_OF_PLACES = 10;
/* 17:   */   
/* 18:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval numberVE)
/* 19:   */   {
/* 20:   */     String hex;
/* 21:   */     String hex;
/* 22:47 */     if ((numberVE instanceof RefEval))
/* 23:   */     {
/* 24:48 */       RefEval re = (RefEval)numberVE;
/* 25:49 */       hex = OperandResolver.coerceValueToString(re.getInnerValueEval(re.getFirstSheetIndex()));
/* 26:   */     }
/* 27:   */     else
/* 28:   */     {
/* 29:51 */       hex = OperandResolver.coerceValueToString(numberVE);
/* 30:   */     }
/* 31:   */     try
/* 32:   */     {
/* 33:54 */       return new NumberEval(BaseNumberUtils.convertToDecimal(hex, 16, 10));
/* 34:   */     }
/* 35:   */     catch (IllegalArgumentException e) {}
/* 36:56 */     return ErrorEval.NUM_ERROR;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 40:   */   {
/* 41:61 */     if (args.length != 1) {
/* 42:62 */       return ErrorEval.VALUE_INVALID;
/* 43:   */     }
/* 44:64 */     return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0]);
/* 45:   */   }
/* 46:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Hex2Dec
 * JD-Core Version:    0.7.0.1
 */