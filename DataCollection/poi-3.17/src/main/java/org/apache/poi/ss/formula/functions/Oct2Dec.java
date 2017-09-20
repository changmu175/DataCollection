/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  4:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  7:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  8:   */ 
/*  9:   */ public class Oct2Dec
/* 10:   */   extends Fixed1ArgFunction
/* 11:   */   implements FreeRefFunction
/* 12:   */ {
/* 13:44 */   public static final FreeRefFunction instance = new Oct2Dec();
/* 14:   */   static final int MAX_NUMBER_OF_PLACES = 10;
/* 15:   */   static final int OCTAL_BASE = 8;
/* 16:   */   
/* 17:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval numberVE)
/* 18:   */   {
/* 19:50 */     String octal = OperandResolver.coerceValueToString(numberVE);
/* 20:   */     try
/* 21:   */     {
/* 22:52 */       return new NumberEval(BaseNumberUtils.convertToDecimal(octal, 8, 10));
/* 23:   */     }
/* 24:   */     catch (IllegalArgumentException e) {}
/* 25:54 */     return ErrorEval.NUM_ERROR;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 29:   */   {
/* 30:59 */     if (args.length != 1) {
/* 31:60 */       return ErrorEval.VALUE_INVALID;
/* 32:   */     }
/* 33:62 */     return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0]);
/* 34:   */   }
/* 35:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Oct2Dec
 * JD-Core Version:    0.7.0.1
 */