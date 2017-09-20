/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  5:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  6:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  7:   */ 
/*  8:   */ public class PPMT
/*  9:   */   extends NumericFunction
/* 10:   */ {
/* 11:   */   public double eval(ValueEval[] args, int srcCellRow, int srcCellCol)
/* 12:   */     throws EvaluationException
/* 13:   */   {
/* 14:34 */     if (args.length < 4) {
/* 15:35 */       throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 16:   */     }
/* 17:39 */     ValueEval v1 = OperandResolver.getSingleValue(args[0], srcCellRow, srcCellCol);
/* 18:40 */     ValueEval v2 = OperandResolver.getSingleValue(args[1], srcCellRow, srcCellCol);
/* 19:41 */     ValueEval v3 = OperandResolver.getSingleValue(args[2], srcCellRow, srcCellCol);
/* 20:42 */     ValueEval v4 = OperandResolver.getSingleValue(args[3], srcCellRow, srcCellCol);
/* 21:   */     
/* 22:44 */     double interestRate = OperandResolver.coerceValueToDouble(v1);
/* 23:45 */     int period = OperandResolver.coerceValueToInt(v2);
/* 24:46 */     int numberPayments = OperandResolver.coerceValueToInt(v3);
/* 25:47 */     double PV = OperandResolver.coerceValueToDouble(v4);
/* 26:   */     
/* 27:49 */     double result = Finance.ppmt(interestRate, period, numberPayments, PV);
/* 28:   */     
/* 29:51 */     checkValue(result);
/* 30:   */     
/* 31:53 */     return result;
/* 32:   */   }
/* 33:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.PPMT
 * JD-Core Version:    0.7.0.1
 */